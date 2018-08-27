INSERT INTO xu_xi_rd2.type (id, name) VALUES (1, 'admin');
INSERT INTO xu_xi_rd2.type (id, name) VALUES (2, 'professional coach');
INSERT INTO xu_xi_rd2.type (id, name) VALUES (3, 'boxer');
INSERT INTO xu_xi_rd2.type (id, name) VALUES (4, 'training coach');
INSERT INTO xu_xi_rd2.type (id, name) VALUES (5, 'duty');
INSERT INTO xu_xi_rd2.type (id, name) VALUES (6, 'sales');

INSERT INTO xu_xi_rd2.user (id, username, password, name, email,type_id) VALUES (0, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin', 'admin@boxing.com',1);

INSERT INTO xu_xi_rd2.authority (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO xu_xi_rd2.authority (id, name) VALUES (2, 'ROLE_USER');

INSERT INTO xu_xi_rd2.user_authority (user_id, authority_id) VALUES (0, 1);

INSERT INTO xu_xi_rd2.labor_law (id, hour,use) VALUES (1, 45,'true');