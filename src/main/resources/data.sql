insert into users values (next value for users_sequence,
                    'Karl', 'Peresau', 'peresau@aaa.ee', '$2a$10$BQM1ZoLmDfkR9H2YCDkjDuWs3UTtJHyQY/5K1luYtkp0VuQ45KIZC', TRUE);
insert into users values (next value for users_sequence,
                    'Jill', 'Jacobs', 'jj@mail.ru', '$2a$10$wbU5UYNt4Yp6xSbk/fdiLuMS38M/8vSJPiC.WJ.jbFTbBQJZ.Fs9i', TRUE);
insert into property values (next value for property_sequence, 'Time of your life', 'Something this', 'uus tn. 5',
                    'http://dijkstra.cs.ttu.ee/~egpalk/tehnika/1.jpg', 3, 2, 1, 200);
insert into property values (next value for property_sequence, 'Best room for best price', 'Lovely room', 'Tallinn',
                    'http://dijkstra.cs.ttu.ee/~egpalk/tehnika/2.jpg', 3, 2, 1, 100);
insert into property values (next value for property_sequence, 'Have to Love this', 'Located in the heart of real nature',
                    'Lootuse tee','http://dijkstra.cs.ttu.ee/~egpalk/tehnika/3.jpg', 3, 2, 1, 300);
insert into property values (next value for property_sequence, 'Good place for a company', 'Some description', 'Vanalinn',
                    'http://dijkstra.cs.ttu.ee/~egpalk/tehnika/4.jpg', 3, 2, 1, 500);
INSERT INTO authorities VALUES ('peresau@aaa.ee', 'ROLE_USER');
INSERT INTO authorities VALUES ('jj@mail.ru', 'ROLE_ADMIN');