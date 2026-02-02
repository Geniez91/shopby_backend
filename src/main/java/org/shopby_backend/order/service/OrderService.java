package org.shopby_backend.order.service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.exception.order.*;
import org.shopby_backend.order.dto.OrderGetByUserIdDto;
import org.shopby_backend.order.dto.OrderInputDto;
import org.shopby_backend.order.dto.OrderOutputDto;
import org.shopby_backend.order.model.OrderEntity;
import org.shopby_backend.order.model.OrderItemEntity;
import org.shopby_backend.order.model.OrderItemId;
import org.shopby_backend.order.persistence.OrderItemRepository;
import org.shopby_backend.order.persistence.OrderRepository;
import org.shopby_backend.order.tools.OrderTools;
import org.shopby_backend.status.model.StatusEntity;
import org.shopby_backend.status.persistence.StatusRepository;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.persistence.UsersRepository;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;

@Slf4j
@AllArgsConstructor
@Service
public class OrderService {
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private UsersRepository usersRepository;
    private StatusRepository statusRepository;
    private ArticleRepository articleRepository;
    private OrderTools orderTools;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderOutputDto addNewOrder(OrderInputDto orderInputDto) {
        if (orderInputDto.deliveryAddress() == null) {
            throw new OrderCreateException("La commande ne peut pas avoir une adresse vide");
        }
        if (orderInputDto.totalPrice() == null) {
            throw new OrderCreateException("La commande ne peut pas avoir une prix vide");
        }
        if (orderInputDto.articlesQuantity() == null || orderInputDto.articlesQuantity().isEmpty()) {
            throw new OrderCreateException("La liste d'article ne peut pas être null");
        }
        if (orderInputDto.idUser() == null) {
            throw new OrderCreateException("La commande ne peut pas avoir un id user null");
        }
        UsersEntity users = usersRepository.findById(orderInputDto.idUser()).orElseThrow(() -> new OrderCreateException("La commande ne peut pas avoir un utilisateur inexistant"));
        StatusEntity status = statusRepository.findByLibelle("Commandés").orElseThrow(() -> new OrderCreateException("La commande ne peut pas avoir un status inexistant"));

        LocalDate currentDate = LocalDate.now();
        LocalDate dateDelivery = currentDate.plusDays(2);

        OrderEntity orderEntity = OrderEntity.builder()
                .deliveryAdress(orderInputDto.deliveryAddress())
                .totalPrice(orderInputDto.totalPrice())
                .status(status)
                .dateOrder(currentDate)
                .dateDelivery(dateDelivery)
                .user(users)
                .build();

        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);

        List<OrderItemEntity> items = orderInputDto.articlesQuantity().stream().map(article -> {
            ArticleEntity articleEntity = articleRepository.findById(article.getIdArticle()).orElseThrow(() -> new OrderCreateException("L'article n'existe pas"));
            return OrderItemEntity.builder()
                    .orderItemId(new OrderItemId(savedOrderEntity.getIdOrder(), articleEntity.getIdArticle()))
                    .article(articleEntity)
                    .order(savedOrderEntity)
                    .quantity(article.getQuantity())
                    .unitPrice(articleEntity.getPrice())
                    .build();
        }).toList();
        orderItemRepository.saveAll(items);
        return orderTools.getOrderOutputDto(savedOrderEntity, items);
    }

    @Scheduled(cron = "0 0 2 * * *") // tous les jours à 02:00
    public void updateStatusOrder() {
        LocalDate local = LocalDate.now();
        List<OrderEntity> orders = Collections.singletonList(orderRepository.findByDateOrderAndStatus_libelle(local, "Commandés").orElseThrow(() -> new OrderUpdateException("Aucun elements ne correspond au filtre")));
        StatusEntity status = statusRepository.findByLibelle("Livraison en cours").orElseThrow(() -> new OrderUpdateException("Aucun status ne correspond a livraison en cours"));
        for (OrderEntity order : orders) {
            order.setStatus(status);
        }
        orderRepository.saveAll(orders);
    }

    public OrderOutputDto deleteOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> new OrderDeleteException("Aucun order ne correspond au order Id"));
        List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrder_IdOrder(order.getIdOrder()).orElseThrow(() -> new OrderDeleteException("Aucun order item ne correspond a votre order id"));
        orderItemRepository.deleteAll(orderItemEntities);
        orderRepository.deleteById(orderId);
        return orderTools.getOrderOutputDto(order, orderItemEntities);
    }

    public OrderOutputDto getOrder(Long orderId) {
        long start = System.nanoTime();
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() -> {
            OrderGetException exception = new OrderGetException("Aucune commande ne correspond à l'id " + orderId);
            log.error("Le orderId ne correspondent pas un commande spécifique : {}",orderId,exception);
            return exception;
        });
        List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrder_IdOrder(orderEntity.getIdOrder()).orElseThrow(() -> new OrderDeleteException("Aucun order item ne correspond a votre order id"));
        OrderOutputDto result = orderTools.getOrderOutputDto(orderEntity, orderItemEntities);
        long durationMs = (System.nanoTime() - start) / 1_000_000;
        logger.info("La commande a bien été bien affiché : orderId={}, durationMs={}", orderId,durationMs);
        return result;
    }

    public List<OrderOutputDto> getOrdersByUserId(OrderGetByUserIdDto orderGetByUserIdDto) {
        long start = System.nanoTime();
        if(orderGetByUserIdDto.userId() == null){
            throw new OrderGetByUserIdException("Le userId ne peut pas etre null");
        }
        List<OrderEntity> listOrderEntity = orderRepository.findByUser_Id(orderGetByUserIdDto.userId()).orElseThrow(()-> new OrderGetByUserIdException("Aucune commande ne correspond au userId"));

        List<OrderOutputDto> result=  listOrderEntity.stream().map(order -> {
            List<OrderItemEntity> items = orderItemRepository.findByOrder_IdOrder(order.getIdOrder()).orElseThrow(()->new OrderGetByUserIdException("Aucune article ne correspond à la commande"));
            return orderTools.getOrderOutputDto(order, items);
        }).toList();
        long durationMs = (System.nanoTime() - start) / 1_000_000;
        logger.info("Les commandes ont été bien affiché : userId={}, orderCount={}, durationMs={}", orderGetByUserIdDto.userId(), result.size(), durationMs);
        return result;
    }


}
