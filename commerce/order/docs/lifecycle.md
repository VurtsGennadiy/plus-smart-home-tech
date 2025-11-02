
---

# Цикл жизни заказа

## 1. Создание нового заказа → `PUT /order`
- Обращаемся к складу - проверяем наличие остатков и получаем габариты → `PUT /warehouse/check`
- Статус _NEW_
---

## 2. Расчёт стоимости

### 2.1 Расчёт стоимости доставки → `POST /order/calculate/delivery(orderId)`
- Запрашиваем адрес склада → `GET /warehouse/address`
- Обращаемся к сервису доставки → `POST /delivery/cost`

### 2.2 Расчёт стоимости товаров → `POST /order/calculate/product(orderId)`
- Обращаемся к сервису оплаты → `POST /payment/productCost(orderDto)`  
  *(обращение к сервису `shopping-store`)*

### 2.3 Расчёт полной стоимости → `POST /order/calculate/total(orderId)`
- Уже должны быть рассчитаны стоимость доставки и товаров
- Обращаемся к сервису оплаты → `POST /payment/totalCost(orderDto)`

---
## 3. Оплата

### 3.1 Инициализация процесса оплаты → `POST /order/payment` (`orderId`)
- Устанавливаем статус заказа: **`ON_PAYMENT`**
- Обращаемся к сервису оплаты  → `post /payment`

### 3.2 Успешная оплата → `POST /payment/success` (`paymentId`)
- Обращаемся к сервису заказа → `POST /order/payment/success`  
- Устанавливаем статус заказа: **`PAID`**

### 3.3 Неудачная оплата → `POST /payment/failed` (`paymentId`)
- Обращаемся к сервису заказа → `POST /order/payment/failed`  
- Устанавливаем статус заказа: **`PAYMENT_FAILED`**


---
## 4. Сборка заказа
> ⚠️ Дальнейшие шаги возможны **только при статусе `PAID`**.

### 4.1 Инициализация сборки заказа → `POST /order/assembly`
- Устанавливаем статус заказа: **`ON_ASSEMBLY`**
- Обращаемся к складу → `POST /warehouse/assembly`
- Проверяем наличие, уменьшаем остатки, создаём бронь

### 4.2 Сборка завершена успешно → `POST /warehouse/assembly/successful`
- Обращаемся к сервису заказа → `POST /order/assembly/success`  
- Устанавливаем статус заказа: **`ASSEMBLED`**

### 4.3 Сборка завершена с ошибкой → `POST /warehouse/assembly/failed`
- Обращаемся к сервису заказа → `POST /order/assembly/failed`  
- Устанавливаем статус заказа: **`ASSEMBLY_FAILED`**

---

## 5. Доставка заказа
> ⚠️ Дальнейшие шаги возможны **только при статусе `ASSEMBLED`**.

## 5.1 Инициализация доставки → `POST /order/delivery`
- Запрашиваем адрес склада → `GET /warehouse/address`
- Обращаемся к сервису доставки → `PUT /delivery` (`DeliveryDto`)
- Создаётся объект доставки
- Устанавливаем статус заказа: **`DELIVERY_CREATED`**

### 5.2 Передать товары в доставку → `POST /delivery/picked` (`orderId`)
- Статус доставки: **`IN_PROGRESS`**
- Забираем товары со склада → `POST /warehouse/shipped` (`ShippedToDeliveryRequest`)
- Обращаемся к сервису заказа → `POST /order/delivery/shipped`  
- Устанавливаем статус заказа: **`ON_DELIVERY`**

### 5.3 Доставка успешно завершена → `POST /delivery/successful` (`orderId`)
- Статус доставки: **`DELIVERED`**
- Обращаемся к сервису заказа  → `POST /order/delivery/success` (`orderId`)  
- Устанавливаем статус заказа: **`DELIVERED`**

### 5.4 Доставка неудачная → `POST /delivery/failed` (`orderId`)
- Статус доставки: **`FAILED`**
- Обращаемся к сервису заказа  → `POST /order/delivery/failed` (`orderId`)  
- Устанавливаем статус заказа: **`DELIVERY_FAILED`**

---

## 6. Возврат товара → `POST /order/return` (`ProductReturnRequest`)
- Возвращаем товары на склад → `POST /warehouse/return` (`Map<Товар, Количество>`)
- Устанавливаем статус заказа: **`PRODUCT_RETURNED`**

---

## 7. Успешное завершение заказа → `POST /order/completed` (`orderId`)
- Заказ можно завершить **только** в одном из следующих статусов:
  - `DELIVERED`
  - `ASSEMBLED` (доставка не понадобилась)
  - `PRODUCT_RETURNED`
- Устанавливаем статус заказа: **`COMPLETED`**
---