INSERT INTO st_url_shortener.user (id, email, username, password, role, first_name, last_name, phone_number, image_url, created_at, updated_at) VALUES (1, 'admin@admin.com', 'admin', '$2a$10$QIvLkO0acZTblAkcAyLSNOuIrZTkI8Bvm4SR1HuanC5Nnc6ZDlvZK', 'ADMIN', 'Super', 'Admin', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO st_url_shortener.user (id, email, username, password, role, first_name, last_name, phone_number, image_url, created_at, updated_at) VALUES (2, 'paajake+user@gmail.com', 'paajake_user', '$2a$10$Gy6nY7CjdDSZTSrJXvUMP.eY4nv26wJbhRwr7DucesOqSR1PYFq3W', 'USER', 'Paa', 'JAKE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO st_url_shortener.user (id, email, username, password, role, first_name, last_name, phone_number, image_url, created_at, updated_at) VALUES (3, 'paajake+admin@gmail.com', 'paajake_admin', '$2a$10$e0djqGOJYfz8rTXd3c37OuRbT/9WreR2KgFdLqnLNzsGtmm04yep2', 'ADMIN', 'Paa', 'JAKE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO st_url_shortener.url (id, full_url, short_url_path, clicks, user_id, created_at, updated_at) VALUES (1, 'https://google.com', 'myg', 0, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

