INSERT INTO "users" ("id", "name", "password", "created", "version")
VALUES (nextval ('hibernate_sequence'), 'admin', '$2a$10$7b.9iLgXFVh.r1u9HEbMv.EDL3JcJgldsWHUg4etSUh4wCNGuExye', NOW(), 0)
ON CONFLICT DO NOTHING;
INSERT INTO "user_roles" ("id", "role") VALUES (0, 'admin')
ON CONFLICT DO NOTHING;
INSERT INTO "user_roles" ("id", "role") VALUES (0, 'user')
ON CONFLICT DO NOTHING;
INSERT INTO "users" ("id", "name", "password", "created", "version")
VALUES (nextval ('hibernate_sequence'), 'user', '$2a$10$7b.9iLgXFVh.r1u9HEbMv.EDL3JcJgldsWHUg4etSUh4wCNGuExye', NOW(), 0)
ON CONFLICT DO NOTHING;
INSERT INTO "user_roles" ("id", "role") VALUES (1, 'user')
ON CONFLICT DO NOTHING;
INSERT INTO "projects" ("id", "name", "user_id", "created", "version")
VALUES (nextval ('hibernate_sequence'), 'Work', 0, NOW(), 0)
ON CONFLICT DO NOTHING;

CREATE SEQUENCE hibernate_sequence START 1;
ALTER SEQUENCE IF EXISTS hibernate_sequence RESTART WITH 10;