package org.shopby_backend.order.service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.article.service.ArticleService;
import org.shopby_backend.exception.order.*;
import org.shopby_backend.order.dto.OrderGetByUserIdDto;
import org.shopby_backend.order.dto.OrderInputDto;
import org.shopby_backend.order.dto.OrderOutputDto;
import org.shopby_backend.order.mapper.OrderItemMapper;
import org.shopby_backend.order.mapper.OrderMapper;
import org.shopby_backend.order.model.OrderEntity;
import org.shopby_backend.order.model.OrderItemEntity;
import org.shopby_backend.order.model.OrderStatus;
import org.shopby_backend.order.persistence.OrderItemRepository;
import org.shopby_backend.order.persistence.OrderRepository;
import org.shopby_backend.order.tools.OrderTools;
import org.shopby_backend.status.model.StatusEntity;
import org.shopby_backend.status.service.StatusService;
import org.shopby_backend.tools.LogMessages;
import org.shopby_backend.tools.Tools;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.service.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UsersService usersService;
    private final StatusService statusService;
    private final ArticleService articleService;
    private final OrderTools orderTools;

    @Transactional
    public OrderOutputDto addNewOrder(OrderInputDto orderInputDto) {
        long start = System.nanoTime();

        UsersEntity users = usersService.findUsersOrThrow(orderInputDto.idUser());

        String statusOrder="Commandés";
        StatusEntity status = statusService.findStatusByLibelleOrThrow(statusOrder);

        LocalDate currentDate = LocalDate.now();
        LocalDate dateDelivery = currentDate.plusDays(2);

        OrderEntity orderEntity = orderMapper.toEntity(orderInputDto,status,currentDate,dateDelivery, users);

        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);

        List<OrderItemEntity> items = orderInputDto.articlesQuantity().stream().map(article -> {
            ArticleEntity articleEntity = articleService.findArticleOrThrow(article.getIdArticle());
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
        List<OrderEntity> orders = this.findOrdersByDateOrderAndStatus(local, OrderStatus.valueOf(statusOrder.name()));

        StatusEntity status = statusService.findStatusByLibelleOrThrow(statusOrder.name());

        for (OrderEntity order : orders) {
            order.setStatus(status);
        }
        orderRepository.saveAll(orders);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le nombre de orders {} qui ont été mis à jour leur statut en {}, durationMs={}",orders.size(),status,durationMs);
    }

    public OrderOutputDto deleteOrder(Long orderId) {
        long start = System.nanoTime();
        OrderEntity order = this.findOrderOrThrow(orderId);

        List<OrderItemEntity> orderItemEntities = this.findOrderItemByOrderIdOrThrow(orderId);

        orderItemRepository.deleteAll(orderItemEntities);
        orderRepository.deleteById(orderId);
        long durationMs = Tools.getDurationMs(start);
        log.info("Order {} supprimer ainsi que tous les articles de cette commande {}, durationMs={}",orderId,orderItemEntities.size(),durationMs);
        return orderTools.getOrderOutputDto(order, orderItemEntities);
    }

    public OrderOutputDto getOrder(Long orderId) {
        long start = System.nanoTime();
        OrderEntity orderEntity = this.findOrderOrThrow(orderId);
        List<OrderItemEntity> orderItemEntities = this.findOrderItemByOrderIdOrThrow(orderId);
        OrderOutputDto result = orderTools.getOrderOutputDto(orderEntity, orderItemEntities);
        long durationMs = Tools.getDurationMs(start);
        log.info("La commande a bien été bien affiché : orderId={}, durationMs={}", orderId,durationMs);
        return result;
    }

    public Page<OrderOutputDto> getOrdersByUserId(OrderGetByUserIdDto orderGetByUserIdDto,Pageable pageable) {
        long start = System.nanoTime();
        Page<OrderEntity> orderPage = this.findOrdersByUserId(orderGetByUserIdDto.userId(),pageable);
        Page<OrderOutputDto> result=  orderPage.map(order -> orderTools.getOrderOutputDto(order, order.getOrderItems()));

        long durationMs = Tools.getDurationMs(start);
        log.info("Les commandes ont été bien affiché : userId={}, orderCount={},page={}, durationMs={}", orderGetByUserIdDto.userId(), result.getNumberOfElements(),result.getNumber(), durationMs);
        return result;
    }

    public OrderEntity findOrderOrThrow(Long orderId) {
       return orderRepository.findById(orderId).orElseThrow(() -> {
            OrderNotFoundException exception= OrderNotFoundException.byOrderId(orderId);
            log.warn(LogMessages.ORDER_NOT_FOUND_BY_ID,orderId,exception);
            return exception;
        });
    }

    public Page<OrderEntity>findOrdersByUserId(Long userId, Pageable pageable) {
      return orderRepository.findByUser_Id(userId,pageable);
    }

    public List<OrderEntity>findOrdersByDateOrderAndStatus(LocalDate local,OrderStatus status){
        return Collections.singletonList(orderRepository.findByDateOrderAndStatus_libelle(local, String.valueOf(status)).orElseThrow(() ->
        {
            OrderNotFoundException exception = OrderNotFoundException.byStatusAndLocalDate(status,local);
            log.warn(LogMessages.ORDER_NOT_FOUND_BY_DATE_AND_STATUS_ORDER,local,status,exception);
            return exception;
        }));
    }

    public List<OrderItemEntity> findOrderItemByOrderIdOrThrow(Long orderId) {
       return orderItemRepository.findByOrder_IdOrder(orderId).orElseThrow(() -> {
            OrderItemNotFoundException exception = new OrderItemNotFoundException(orderId);
            log.warn(LogMessages.ORDER_NOT_FOUND_BY_ID,orderId,exception);
            return exception;
        });
    }
}


