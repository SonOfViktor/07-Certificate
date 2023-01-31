INSERT INTO categories (id, name, picture)
VALUES (1, 'Food', 'category/Food.jpg'),
       (2, 'Sport', 'category/Sport.jpg'),
       (3, 'Tech', 'category/Tech.jpg'),
       (4, 'Hobby', 'category/Hobby.jpg');

INSERT INTO gift_certificates
(id, name, description, price, duration, image, create_date, last_update_date, category_id)
VALUES (1, 'Certificate1', 'Very good certificate', 45, 15, 'certificate/Certificate12023-01-30T01-45-00.000.jpg',
        '2023-01-30T01:45:00.000', '2023-01-30T01:45:00.000', 1),
       (2, 'Certificate2', 'Very good certificate', 15, 20, 'certificate/Certificate22023-01-30T01-45-01.000.jpg',
        '2023-01-30T01:45:01.000', '2023-01-30T01:45:01.000', 2),
       (3, 'Certificate3', 'Very good certificate', 100, 15, 'certificate/Certificate32023-01-30T01-45-02.000.jpg',
        '2023-01-30T01:45:02.000', '2023-01-30T01:45:02.000', 3),
       (4, 'Certificate4', 'Very good certificate', 800, 99, 'certificate/Certificate42023-01-30T01-45-03.000.jpg',
        '2023-01-30T01:45:03.000', '2023-01-30T01:45:03.000', 4),
       (5, 'Certificate5', 'Very good certificate', 55, 15, 'certificate/Certificate52023-01-30T01-45-04.000.jpg',
        '2023-01-30T01:45:04.000', '2023-01-30T01:45:04.000', 4),
       (6, 'Certificate6', 'Very good certificate', 45, 7, 'certificate/Certificate62023-01-30T01-45-05.000.jpg',
        '2023-01-30T01:45:05.000', '2023-01-30T01:45:05.000', 3),
       (7, 'Certificate7', 'Very good certificate', 45, 15, 'certificate/Certificate72023-01-30T01-45-06.000.jpg',
        '2023-01-30T01:45:06.000', '2023-01-30T01:45:06.000', 2),
       (8, 'Certificate8', 'Very good certificate', 90, 14, 'certificate/Certificate82023-01-30T01-45-07.000.jpg',
        '2023-01-30T01:45:07.000', '2023-01-30T01:45:07.000', 1),
       (9, 'Certificate9', 'Very good certificate', 45, 15, 'certificate/Certificate92023-01-30T01-45-08.000.jpg',
        '2023-01-30T01:45:08.000', '2023-01-30T01:45:08.000', 1),
       (10, 'Certificate10', 'Very good certificate', 100, 70, 'certificate/Certificate102023-01-30T01-45-09.000.jpg',
        '2023-01-30T01:45:09.000', '2023-01-30T01:45:09.000', 4),
       (11, 'Certificate11', 'Very good certificate', 45, 15, 'certificate/Certificate112023-01-30T01-45-10.000.jpg',
        '2023-01-30T01:45:10.000', '2023-01-30T01:45:10.000', 2);

INSERT INTO tags (id, name)
VALUES (1, 'food'),
       (2, 'sport'),
       (3, 'tech'),
       (5, 'hobby'),
       (6, 'summer'),
       (7, 'piece'),
       (8, 'java'),
       (9, 'game'),
       (10, 'phone');

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (1, 1),
       (1, 9),
       (1, 3),
       (2, 8),
       (3, 1),
       (3, 5),
       (7, 2),
       (8, 10),
       (10, 10);

-- password = 123
INSERT INTO users (id, email, password, first_name, last_name, role)
VALUES (1, 'user1@gmail.com', '$2a$12$LeqV6NLyRa9SjC4dCvTDFupSkgp67g4x2uoCUWEyg4SpckO20Hp1e', 'Maria', 'First', 'ADMIN'),
       (2, 'user2@gmail.com', '$2a$12$LeqV6NLyRa9SjC4dCvTDFupSkgp67g4x2uoCUWEyg4SpckO20Hp1e', 'Viktor', 'Second', 'USER'),
       (3, 'user3@gmail.com', '$2a$12$LeqV6NLyRa9SjC4dCvTDFupSkgp67g4x2uoCUWEyg4SpckO20Hp1e', 'Sergey', 'Third', 'USER');

INSERT INTO payments(id, user_id, created_date)
VALUES (1, 1, '2023-01-30T01:10:08.000'),
       (2, 1, '2023-01-30T01:44:08.000'),
       (3, 2, '2023-01-30T01:45:08.000');

INSERT INTO orders(id, cost, gift_certificate_id, payment_id)
VALUES (1, 45, 1, 1),
       (2, 15, 2, 1),
       (3, 15, 2, 1),
       (4, 100, 3, 1),
       (5, 45, 6, 2),
       (6, 45, 7, 2),
       (7, 45, 7, 3);