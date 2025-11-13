# Тестовые JSON запросы для API

## Эндпоинт: POST /api/v1/orders/batch-create

### batch-create-valid.json
Валидный запрос с двумя заказами:
- Первый заказ: 1 товар (коврик для мышки)
- Второй заказ: 2 товара (клавиатура + мышь)
- Все суммы корректно рассчитаны

### batch-create-invalid.json
Невалидный запрос для проверки валидации:
- `totalPriceCents` = 18001, но сумма товаров = 18000 (9000 * 2)
- Должен вернуть ошибку: `"Orders[0]": ["TotalPriceCents should be equal to sum of all OrderItems.PriceCents * OrderItems.Quantity"]`

## Эндпоинт: POST /api/v1/orders/query

### query-all.json
Получить все заказы с пагинацией (первые 10)

### query-by-ids.json
Получить заказы по конкретным ID (1, 2, 3)

### query-by-customer-ids.json
Получить заказы по ID клиентов (123, 456)

### query-combined.json
Комбинированный запрос: фильтр по ID заказов И ID клиентов

### query-pagination.json
Запрос с пагинацией (пропустить первые 10, взять следующие 5)

## Как использовать:

1. **Через Swagger UI**: 
   - Откройте http://localhost:8080/swagger-ui.html
   - Выберите эндпоинт
   - Скопируйте JSON из файла в поле запроса

2. **Через curl**:
   ```bash
   curl -X POST http://localhost:8080/api/v1/orders/batch-create \
     -H "Content-Type: application/json" \
     -d @test-requests/batch-create-valid.json
   ```

3. **Через Postman**:
   - Импортируйте JSON файлы как примеры запросов

