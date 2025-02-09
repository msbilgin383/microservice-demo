# Microservice Demo Project

Bu proje, modern mikroservis mimarisi kullanılarak geliştirilmiş bir demo uygulamasıdır. Proje, kitap ve müşteri yönetimi için REST API'ler sunar.

## Servisler

- **Discovery Service** (Port: 8761): Eureka Server - Servis kaydı ve keşfi için
- **Gateway Service** (Port: 8080): API Gateway - Routing ve load balancing için
- **Book Service** (Port: 8082): Kitap yönetimi API'si
- **Customer Service** (Port: 8083): Müşteri yönetimi API'si
- **Cache Service** (Port: 8084): Redis cache yönetimi

## Teknolojiler

- Java 17
- Spring Boot 3.x
- Spring Cloud
- MongoDB
- Redis
- Docker
- Maven

## Başlangıç

### Gereksinimler

- Java 17 veya üzeri
- Maven
- Docker
- MongoDB
- Redis

### Kurulum

1. MongoDB'yi başlatın:
```bash
docker run -d -p 27017:27017 --name mongodb mongo
```

2. Redis'i başlatın:
```bash
docker run -d -p 6379:6379 --name redis redis
```

3. Discovery Service'i başlatın:
```bash
cd discovery-service
mvn spring-boot:run
```

4. Gateway Service'i başlatın:
```bash
cd gateway-service
mvn spring-boot:run
```

5. Book Service'i başlatın:
```bash
cd book-service
mvn spring-boot:run
```

6. Customer Service'i başlatın:
```bash
cd customer-service
mvn spring-boot:run
```

## API Dokümantasyonu

### Book Service API (localhost:8082)

#### Endpoints

- `GET /api/v1/books`: Tüm kitapları listele (Sayfalama ve filtreleme destekli)
  - Query Parameters:
    - `page`: Sayfa numarası (default: 0)
    - `size`: Sayfa boyutu (default: 10)
    - `title`: Kitap başlığına göre filtrele (opsiyonel)
    - `author`: Yazara göre filtrele (opsiyonel)

- `GET /api/v1/books/{id}`: ID'ye göre kitap getir

- `POST /api/v1/books`: Yeni kitap ekle
  ```json
  {
    "title": "Kitap Adı",
    "author": "Yazar Adı",
    "price": 29.99,
    "stock": 100
  }
  ```

- `PUT /api/v1/books/{id}`: Kitap güncelle
- `DELETE /api/v1/books/{id}`: Kitap sil

### Swagger UI

Her servis için Swagger UI dokümantasyonuna aşağıdaki URL'lerden erişilebilir:

- Book Service: http://localhost:8082/swagger-ui.html
- Customer Service: http://localhost:8083/swagger-ui.html

### Performans İyileştirmeleri

1. **Caching**
   - Redis cache entegrasyonu
   - Kitap detayları için önbellekleme
   - Cache TTL: 1 saat

2. **Database**
   - MongoDB indeksleri (title ve author için)
   - Compound index kullanımı

3. **API**
   - Sayfalama desteği
   - Filtreleme özellikleri
   - Validation
   - Exception handling

## Monitoring

- Actuator endpoints aktif
- Prometheus metrics desteği
- Distributed tracing için Sleuth/Zipkin entegrasyonu

## Güvenlik

- Input validation
- Rate limiting
- Security headers

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakınız.
