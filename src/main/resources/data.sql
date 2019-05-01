insert into users values (next value for users_sequence,
                    'Karl', 'Peresau', 'peresau', '$2a$10$BQM1ZoLmDfkR9H2YCDkjDuWs3UTtJHyQY/5K1luYtkp0VuQ45KIZC', TRUE);
insert into users values (next value for users_sequence,
                    'Jill', 'Jacobs', 'jj', '$2a$10$L5svqV.HSC1Nz5uGseDHPOubETI45TN9L8JJDLZPo091jsA6oH2Wa', TRUE);
insert into property (property_id, title, description, address, county, municipality, settlement,
                      street, pic_url, room_count, bed_count, owner_id, price)
       values (next value for property_sequence, 'Time of your life', 'Something this', 'uus tn. 5',
                      'Harjumaa', 'Tallinn', 'Nõmme', 'Mai',
                      'https://firebasestorage.googleapis.com/v0/b/tarkvaratehnika-1551709647803.appspot.com/o/images%2Ffdkjgflkfdmlkgmlkfjygkjtrkjgkrkgmn%2Cds%2Cf%2C.jpg?alt=media&token=6b67b60c-6316-4145-8115-89010a7be338', 3, 2, 2, 200);
insert into property (property_id, title, description, address, county, municipality, settlement,
                      street, pic_url, room_count, bed_count, owner_id, price)
       values (next value for property_sequence, 'Best room for best price', 'Lovely room', 'Tallinn',
                      'Harjumaa', 'Tallinn', 'Kadriorg', 'Vesivärava',
                      'https://firebasestorage.googleapis.com/v0/b/tarkvaratehnika-1551709647803.appspot.com/o/images%2Ffkjdskfnkjdsngkjnejgnjdfnj.jpg?alt=media&token=8d8e84a0-de92-4c49-a8c9-84f32136d77d', 4, 2, 1, 100);
insert into property (property_id, title, description, address, county, municipality, settlement,
                      street, pic_url, room_count, bed_count, owner_id, price)
       values (next value for property_sequence, 'Have to Love this', 'Located in the heart of real nature','Lootuse tee',
                      'Tartumaa', 'Tartu', 'Annelinn', 'Sõbra',
                      'https://firebasestorage.googleapis.com/v0/b/tarkvaratehnika-1551709647803.appspot.com/o/images%2Ffsdkfjmksdmngknfskdjgnkjnsdkjgfnkfdmn.jpg?alt=media&token=1a27b033-33f3-4ffb-b821-9657b239eafc', 3, 2, 1, 300);
insert into property (property_id, title, description, address, county, municipality, settlement,
                      street, pic_url, room_count, bed_count, owner_id, price)
       values (next value for property_sequence, 'Good place for a company', 'Some description', 'Vanalinn',
                      'Harjumaa', 'Tallinn', 'Mustamäe', 'Vilde tee',
                      'https://firebasestorage.googleapis.com/v0/b/tarkvaratehnika-1551709647803.appspot.com/o/images%2Fvbgfhbgfnjfksdjkfjlkdmhkbgfkhmlkgfmhgflkhmlkgf.jpg?alt=media&token=ea650c20-420e-47b4-889b-a8237f160de4', 3, 2, 2, 500);
INSERT INTO authorities VALUES ('peresau', 'ROLE_USER');
INSERT INTO authorities VALUES ('jj', 'ROLE_ADMIN');
INSERT INTO Rent (rent_id, property_id, owner_id, renter_username, state, start, end)
       VALUES (next value for rent_sequence, 2, 1, 'peresau', 'TO_RENT', '2089-06-20', '2089-06-30');

-- INSERT INTO Rent (rent_id, property_id, owner_id, renter_username, state, start, end)
--        VALUES (next value for rent_sequence, 4, 1, 'jj', 'CONFIRM_RENT', '2019-6-20', '2019-6-30');

-- rent_id BIGINT PRIMARY KEY,
--   property_id BIGINT NOT NULL,
--   owner_id BIGINT NOT NULL,
--   renter BIGINT NOT NULL,
--   state VARCHAR(30) NOT NULL,
--   start DATE,
--   end DATE,