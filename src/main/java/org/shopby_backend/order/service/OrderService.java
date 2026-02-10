package org.shopby_backend.order.service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.exception.order.*;
import org.shopby_backend.exception.status.StatusNotFoundException;
import org.shopby_backend.exception.users.UsersNotFoundException;
import org.shopby_backend.order.dto.OrderGetByUserIdDto;
import org.shopby_backend.order.dto.OrderInputDto;
import org.shopby_backend.order.dto.OrderOutputDto;
import org.shopby_backend.order.model.OrderEntity;
import org.shopby_backend.order.model.OrderItemEntity;
import org.shopby_backend.order.model.OrderItemId;
import org.shopby_backend.order.model.OrderStatus;
import org.shopby_backend.order.persistence.OrderItemRepository;
import org.shopby_backend.order.persistence.OrderRepository;
import org.shopby_backend.order.tools.OrderTools;
import org.shopby_backend.status.model.StatusEntity;
import org.shopby_backend.status.persistence.StatusRepository;
import org.shopby_backend.tools.Tools;
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

    public OrderOutputDto addNewOrder(OrderInputDto orderInputDto) {
        long start = System.nanoTime();

        UsersEntity users = usersRepository.findById(orderInputDto.idUser()).orElseThrow(() ->
        {
            UsersNotFoundException exception= new UsersNotFoundException("La commande ne peut pas avoir un utilisateur inexistant" + orderInputDto.idUser());
            log.warn("La commande ne peut pas avoir un utilisateur inexistant {}",orderInputDto.idUser(),exception);
            return exception;
        });

        String statusOrder="Commandés";
        StatusEntity status = statusRepository.findByLibelle(statusOrder).orElseThrow(() ->
        {
            StatusNotFoundException exception = new StatusNotFoundException("La commande ne peut pas avoir un status inexistant "+ statusOrder);
            log.warn("La commande ne peut pas avoir un status inexistant {}",statusOrder,exception);
            return exception;
        });

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
        long durationMs = Tools.getDurationMs(start);
        log.info("La commande {} a bien été ajouté avec {} articles, durationMs={}",savedOrderEntity.getIdOrder(),items.size(),durationMs);
        return orderTools.getOrderOutputDto(savedOrderEntity, items);
    }

    @Scheduled(cron = "0 0 2 * * *") // tous les jours à 02:00
    public void updateStatusOrder() {
        long start = System.nanoTime();
        LocalDate local = LocalDate.now();
        OrderStatus statusOrder = OrderStatus.Commandés;
        List<OrderEntity> orders = Collections.singletonList(orderRepository.findByDateOrderAndStatus_libelle(local,statusOrder.name()).orElseThrow(() ->
        {
            OrderNotFoundException exception = new OrderNotFoundException("Aucun elements ne correspond au filtre date order " +local +" et le status order " + statusOrder);
            log.warn("Aucun elements ne correspond au filtre date order {} et le status order {} ",local,statusOrder,exception);
            return exception;
        }));

        StatusEntity status = statusRepository.findByLibelle(OrderStatus.LivraisonEnCours.name()).orElseThrow(() -> {
            StatusNotFoundException exception = new StatusNotFoundException("Aucun status ne correspond à " + OrderStatus.LivraisonEnCours.name());
            log.warn("Aucun status ne correspond à {}", OrderStatus.LivraisonEnCours.name(),exception);
            return exception;
        });

        for (OrderEntity order : orders) {
            order.setStatus(status);
        }
        orderRepository.saveAll(orders);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le nombre de orders {} qui ont été mis à jour leur statut en {}, durationMs={}",orders.size(),status,durationMs);
    }

    public OrderOutputDto deleteOrder(Long orderId) {
        long start = System.nanoTime();
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> {
            OrderNotFoundException exception=new OrderNotFoundException("Aucun order ne correspond au order Id " + orderId);
            log.warn("Le orderId ne correspondent pas un commande spécifique : {}",orderId,exception);
            return exception;
        });

        List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrder_IdOrder(order.getIdOrder()).orElseThrow(() -> {
            OrderItemNotFoundException exception = new OrderItemNotFoundException("Aucun order item ne correspond a votre order id "+ orderId);
            log.warn("Aucun order item ne correspond au order Id  + orderId : {}",orderId,exception);
            return exception;
        });

        orderItemRepository.deleteAll(orderItemEntities);
        orderRepository.deleteById(orderId);
        long durationMs = Tools.getDurationMs(start);
        log.info("Order {} supprimer ainsi que tous les articles de cette commande {}, durationMs={}",orderId,orderItemEntities.size(),durationMs);
        return orderTools.getOrderOutputDto(order, orderItemEntities);
    }

    public OrderOutputDto getOrder(Long orderId) {
        long start = System.nanoTime();
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() -> {
            OrderNotFoundException exception = new OrderNotFoundException("Aucune commande ne correspond à l'id " + orderId);
            log.warn("Le orderId ne correspondent pas un commande spécifique : {}",orderId,exception);
            return exception;
        });
        List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrder_IdOrder(orderEntity.getIdOrder()).orElseThrow(() -> new OrderDeleteException("Aucun order item ne correspond a votre order id"));
        OrderOutputDto result = orderTools.getOrderOutputDto(orderEntity, orderItemEntities);
        long durationMs = Tools.getDurationMs(start);
        log.info("La commande a bien été bien affiché : orderId={}, durationMs={}", orderId,durationMs);
        return result;
    }

    public List<OrderOutputDto> getOrdersByUserId(OrderGetByUserIdDto orderGetByUserIdDto) {
        long start = System.nanoTime();

        List<OrderEntity> listOrderEntity = orderRepository.findByUser_Id(orderGetByUserIdDto.userId()).orElseThrow(()->
        {
            OrderNotFoundException exception = new OrderNotFoundException("Aucune commande ne correspond au userId "+orderGetByUserIdDto.userId());
            log.warn("Aucune commande ne correspond au userId {}",orderGetByUserIdDto.userId(),exception);
            return exception;
        });

        List<OrderOutputDto> result=  listOrderEntity.stream().map(order -> {
            List<OrderItemEntity> items = orderItemRepository.findByOrder_IdOrder(order.getIdOrder()).orElseThrow(()->
            {
                OrderItemNotFoundException exception = new OrderItemNotFoundException("Aucun article ne correspond à la commande " + order.getIdOrder());
                log.warn("Aucun article ne correspond à la commande {}",order.getIdOrder(),exception);
                return exception;
            });
            return orderTools.getOrderOutputDto(order, items);
        }).toList();
        long durationMs = Tools.getDurationMs(start);
        log.info("Les commandes ont été bien affiché : userId={}, orderCount={}, durationMs={}", orderGetByUserIdDto.userId(), result.size(), durationMs);
        return result;
    }
}
