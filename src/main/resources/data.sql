insert into users values (next value for users_sequence,
                    'Karl', 'Peresau', 'peresau', '$2a$10$BQM1ZoLmDfkR9H2YCDkjDuWs3UTtJHyQY/5K1luYtkp0VuQ45KIZC', TRUE);
insert into users values (next value for users_sequence,
                    'Jill', 'Jacobs', 'jj', '$2a$10$L5svqV.HSC1Nz5uGseDHPOubETI45TN9L8JJDLZPo091jsA6oH2Wa', TRUE);
insert into property values (next value for property_sequence, 'Time of your life', 'Something this', 'uus tn. 5',
                    'http://dijkstra.cs.ttu.ee/~egpalk/tehnika/1.jpg', 3, 2, 1, 200);
insert into property values (next value for property_sequence, 'Best room for best price', 'Lovely room', 'Tallinn',
                    'http://dijkstra.cs.ttu.ee/~egpalk/tehnika/2.jpg', 4, 2, 1, 100);
insert into property values (next value for property_sequence, 'Have to Love this', 'Located in the heart of real nature',
                    'Lootuse tee','http://dijkstra.cs.ttu.ee/~egpalk/tehnika/3.jpg', 3, 2, 1, 300);
insert into property values (next value for property_sequence, 'Good place for a company', 'Some description', 'Vanalinn',
                    'http://dijkstra.cs.ttu.ee/~egpalk/tehnika/4.jpg', 3, 2, 1, 500);
INSERT INTO authorities VALUES ('peresau', 'ROLE_USER');
INSERT INTO authorities VALUES ('jj', 'ROLE_ADMIN');