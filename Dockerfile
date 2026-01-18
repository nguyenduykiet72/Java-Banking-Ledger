├── core-banking-services/           <-- Thư mục chứa các service backend
│   │
│   ├── ledger-service/              <-- ĐÂY LÀ SERVICE BẠN ĐANG HỎI
│   │   ├── pom.xml
│   │   └── src/main/java/com/fintech/ledger/
│   │       ├── domain/              <-- Cấu trúc DDD nằm ở đây
│   │       ├── application/
│   │       ├── infrastructure/
│   │       └── LedgerApplication.java (Main class)
│   │
│   ├── account-service/             <-- Service khác, có cấu trúc RIÊNG của nó
│   │   ├── pom.xml
│   │   └── src/main/java/com/fintech/account/
│   │       ├── domain/              <-- Domain của Account (Khác Ledger!)
│   │       ├── application/
│   │       └── infrastructure/
│   │
│   ├── transfer-service/            <-- Orchestrator Service
│   │   ├── pom.xml
│   │   └── src/main/java/com/fintech/transfer/
│   │       ├── domain/              <-- Logic Saga/Process Manager
│   │       └── ...
│   │
│   └── notification-service/        <-- Service đơn giản (CRUD)
│   ├   ├── pom.xml
│   ├    └── src/main/java/com/fintech/notification/
│   ├        ├── controller/          <-- Có thể dùng cấu trúc MVC đơn giản 3 lớp
│   ├        ├── service/             <-- Không nhất thiết phải ép DDD vào service nhỏ
│   ├        └── repository/
    ├──└── common-service/                <-- Code dùng chung (Cẩn thận khi dùng cái này)
            ├── proto-schema/                <-- Định nghĩa gRPC (.proto files) cho toàn hệ thống
            └── common-utils/                <-- Logging, DateUtils, BaseException
