package org.shopby_backend.order.service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.exception.article.ArticleNotFoundException;
import org.shopby_backend.exception.order.*;
import org.shopby_backend.exception.status.StatusNotFoundException;
import org.shopby_backend.exception.users.UsersNotFoundException;
import org.shopby_backend.order.dto.OrderGetByUserIdDto;
import org.shopby_backend.order.dto.OrderInputDto;
import org.shopby_backend.order.dto.OrderOutputDto;
import org.shopby_backend.order.mapper.OrderItemMapper;
import org.shopby_backend.order.mapper.OrderMapper;
import org.shopby_backend.order.model.OrderEntity;
import org.shopby_backend.order.model.OrderItemEntity;
import org.shopby_backend.order.model.OrderItemId;
import org.shopby_backend.order.model.OrderStatus;
import org.shopby_backend.order.persistence.OrderItemRepository;
import org.shopby_backend.order.persistence.OrderRepository;
import org.shopby_backend.order.tools.OrderTools;
import org.shopby_backend.status.model.StatusEntity;
import org.shopby_backend.status.persistence.StatusRepository;
import org.shopby_backend.tools.LogMessages;
import org.shopby_backend.tools.Tools;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.persistence.UsersRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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
    private OrderMapper orderMapper;
    private OrderItemMapper orderItemMapper;

    public OrderOutputDto addNewOrder(OrderInputDto orderInputDto) {
        long start = System.nanoTime();

        UsersEntity users = usersRepository.findById(orderInputDto.idUser()).orElseThrow(() ->
        {
            UsersNotFoundException exception= UsersNotFoundException.byUserId(orderInputDto.idUser());
            log.warn(LogMessages.USERS_NOT_FOUND_BY_USER_ID,orderInputDto.idUser(),exception);
            return exception;
        });

        String statusOrder="Commandés";
        StatusEntity status = statusRepository.findByLibelle(statusOrder).orElseThrow(() ->
        {
            StatusNotFoundException exception =  StatusNotFoundException.byOrderStatus(OrderStatus.valueOf(statusOrder));
            log.warn(LogMessages.STATUS_NOT_FOUND_BY_ORDER_STATUS,statusOrder,exception);
            return exception;
        });

        LocalDate currentDate = LocalDate.now();
        LocalDate dateDelivery = currentDate.plusDays(2);

        OrderEntity orderEntity = orderMapper.toEntity(orderInputDto,status,currentDate,dateDelivery, users);

        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);

        List<OrderItemEntity> items = orderInputDto.articlesQuantity().stream().map(article -> {
            ArticleEntity articleEntity = articleRepository.findById(article.getIdArticle()).orElseThrow(() -> new ArticleNotFoundException(article.getIdArticle()));
            return orderItemMapper.toEntity(orderEntity,articleEntity,article);
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
            OrderNotFoundException exception = OrderNotFoundException.byStatusAndLocalDate(statusOrder,local);
            log.warn(LogMessages.ORDER_NOT_FOUND_BY_DATE_AND_STATUS_ORDER,local,statusOrder,exception);
            return exception;
        }));

        StatusEntity status = statusRepository.findByLibelle(OrderStatus.LivraisonEnCours.name()).orElseThrow(() -> {
            StatusNotFoundException exception = StatusNotFoundException.byOrderStatus(OrderStatus.LivraisonEnCours);
            log.warn(LogMessages.STATUS_NOT_FOUND_BY_ORDER_STATUS, OrderStatus.LivraisonEnCours.name(),exception);
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
            OrderNotFoundException exception= OrderNotFoundException.byOrderId(orderId);
            log.warn(LogMessages.ORDER_NOT_FOUND_BY_ID,orderId,exception);
            return exception;
        });

        List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrder_IdOrder(order.getIdOrder()).orElseThrow(() -> {
            OrderItemNotFoundException exception = new OrderItemNotFoundException(orderId);
            log.warn(LogMessages.ORDER_NOT_FOUND_BY_ID,orderId,exception);
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
            OrderNotFoundException exception =  OrderNotFoundException.byOrderId(orderId);
            log.warn(LogMessages.ORDER_NOT_FOUND_BY_ID,orderId,exception);
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
            OrderNotFoundException exception = OrderNotFoundException.byUserId(orderGetByUserIdDto.userId());
            log.warn(LogMessages.ORDER_NOT_FOUND_BY_USER_ID,orderGetByUserIdDto.userId(),exception);
            return exception;
        });

        List<OrderOutputDto> result=  listOrderEntity.stream().map(order -> {
            List<OrderItemEntity> items = orderItemRepository.findByOrder_IdOrder(order.getIdOrder()).orElseThrow(()->
            {
                OrderItemNotFoundException exception = new OrderItemNotFoundException(order.getIdOrder());
                log.warn(LogMessages.ORDER_ITEM_NOT_FOUND_BY_ORDER_ID,order.getIdOrder(),exception);
                return exception;
            });
            return orderTools.getOrderOutputDto(order, items);
        }).toList();
        long durationMs = Tools.getDurationMs(start);
        log.info("Les commandes ont été bien affiché : userId={}, orderCount={}, durationMs={}", orderGetByUserIdDto.userId(), result.size(), durationMs);
        return result;
    }
}
