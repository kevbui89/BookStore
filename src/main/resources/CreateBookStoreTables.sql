USE BOOKSTORE;

drop table if exists Book_Genre;
drop table if exists Book_Author;
drop table if exists Answered_Survey;
drop table if exists Survey_Response;
drop table if exists Tax;
drop table if exists Author;
drop table if exists Ad;
drop table if exists Survey;
drop table if exists Response;
drop table if exists Review;
drop table if exists Detail_Invoice;
drop table if exists Master_Invoice;
drop table if exists Book;
drop table if exists EBook_Format;
drop table if exists Publisher;
drop table if exists Client;
drop table if exists Genre;
drop table if exists Country;
drop table if exists Province;
drop table if exists Rss_News;

CREATE TABLE Author (
  author_id int not null auto_increment primary key,
  name varchar(130) not null unique,
  index(name)
);

CREATE TABLE Genre (
  genre_id int not null auto_increment primary key,
  genre varchar(40) not null unique,
  index(genre)
);

CREATE TABLE EBook_Format (
  format_id int not null auto_increment primary key,
  book_format varchar(10)not null unique
);

CREATE TABLE Publisher (
  publisher_id int not null auto_increment primary key,
  publisher varchar(80) not null unique
);

CREATE TABLE Ad (
  ad_id int not null auto_increment primary key,
  title varchar(80) not null,
  image varchar(100),
  ad_link varchar(200),
  chosen tinyint(1) not null default 0
);

CREATE TABLE Survey (
  survey_id int not null auto_increment primary key,
  question varchar(150) not null,
  chosen boolean default false
);

CREATE TABLE Country (
  country_id int not null auto_increment primary key,
  country varchar(60) not null unique
);

CREATE TABLE Response (
  response_id int not null auto_increment primary key,
  total_answer int not null default 0,
  answer varchar(60) not null
);

CREATE TABLE Province (
  province_id int not null auto_increment primary key,
  province varchar(80) not null
);

CREATE TABLE Book (
  book_id int not null auto_increment primary key,
  isbn char(10) not null,
  Title varchar(150) not null,
  description mediumtext,
  pub_date date not null,
  inventory_date date not null,
  page int not null,
  list_price decimal(8,2),
  sale_price decimal(6,2),
  wholesale_price decimal(8,2),
  removed tinyint(1) not null default 0,
  total_sale int not null default 0,
  image varchar(100),
  publisher_id int not null,
  format_id int not null,
  foreign key (publisher_id) references Publisher(publisher_id) on delete cascade,
  foreign key (format_id) references EBook_Format(format_id) on delete cascade,
  index(title)
);

CREATE TABLE Client (
  client_id int not null auto_increment primary key,
  password varchar(30),
  title varchar(40) not null,
  username varchar(80) not null unique,
  email varchar(80) not null unique,
  last_name varchar(80) not null,
  first_name varchar(80) not null,
  company varchar(80) not null,
  address_one varchar(80) not null,
  address_two varchar(80),
  postal_code varchar(15) not null,
  home_tel varchar(20),
  cell_tel varchar(20),
  province_id int not null,
  city varchar(80) not null,
  country_id int not null,
  last_genre_visited int,
  manager tinyint(1) not null default 0,
  first_login tinyint(1) not null default 1,
  foreign key (province_id) references Province(province_id) on delete cascade,
  foreign key (last_genre_visited) references Genre(genre_id) on delete cascade,
  foreign key (country_id) references Country(country_id) on delete cascade,
  index(email)
);

CREATE TABLE Book_Genre (
  book_id int not null,
  genre_id int not null,
  foreign key (book_id) references Book(book_id) on delete cascade,
  foreign key (genre_id) references Genre(genre_id) on delete cascade
);


CREATE TABLE Book_Author (
  book_id int not null,
  author_id int not null,
  foreign key (book_id) references Book(book_id) on delete cascade,
  foreign key (author_id) references Author(author_id) on delete cascade
);

CREATE TABLE Survey_Response (
  survey_id int not null,
  response_id int not null,
  foreign key (survey_id) references Survey(survey_id) on delete cascade,
  foreign key (response_id) references Response(response_id) on delete cascade
);

CREATE TABLE Tax (
  tax_id int not null auto_increment primary key,
  province_id int not null,
  gst decimal(5,3),
  pst decimal(5,3),
  hst decimal(5,3),
  foreign key (province_id) references Province(province_id) on delete cascade
);

CREATE TABLE Review (
  review_id int not null auto_increment primary key,
  client_id int not null,
  book_id int not null,
  review_date timestamp not null default current_timestamp,
  comment_title varchar(150),
  comment varchar(750),
  rating int default null,
  valid tinyint(1) default 0,
  pending  tinyint(1) default 1,
  foreign key (client_id) references Client(client_id) on delete cascade,
  foreign key (book_id) references Book(book_id) on delete cascade
);

CREATE TABLE Master_Invoice (
  invoice_id int not null auto_increment primary key,
  client_id int not null,
  sale_date timestamp not null default current_timestamp,
  net_value decimal(8,2),
  gross_value decimal(8,2),
  foreign key (client_id) references Client(client_id) on delete cascade
);

CREATE TABLE Detail_Invoice (
  detail_id int not null auto_increment primary key,
  invoice_id int not null,
  book_id int not null,
  book_price decimal(8,2),
  gst decimal(4,2),
  pst decimal(4,2),
  hst decimal(4,2),
  status  tinyint(1) default 0,
  foreign key (invoice_id) references Master_Invoice(invoice_id) on delete cascade,
  foreign key (book_id) references Book(book_id) on delete cascade
);

CREATE TABLE Rss_News (
 rss_id int not null auto_increment primary key,
 title varchar(80) not null,
 link varchar(100) not null,
 display  tinyint(1) default 0
);



INSERT INTO EBook_Format(format_id, book_format) VALUES
(1,'AZW'),
(2,'BBEB'),
(3,'CHM'),
(4,'DOC'),
(5,'HTML'),
(6,'PDF'),
(7,'TXT'),
(8, 'EPUB');

INSERT INTO Author(author_id, name) VALUES
(1,'Walter Isaacson'),
(2,'Pete Souza'),
(3,'Barack Obama'),
(4,'Ron Chernow'),
(5,'MALCOLM X'),
(6,'Nelson Mandela'),
(7,'Maximilian Mizzi'),
(8,'Julie Chadwick'),
(9,'James A. Connor'),
(10,'William Taubman'),
(11,'L. David Marquet'),
(12,'Mohandas K. Gandhi'),
(13,'Jon Lee Anderson'),
(14,'Jonathan Fenby'),
(15,'Clayborne Carson'),
(16,'Pamela Geller'),
(17,'Eben Alexander M.D.'),
(18,'Warren Zanes'),
(19,'John Agard'),
(20,'Arnold Schwarzenegger'),
(21,'Frank Zappa'),
(22,'Neil Packer'),
(23,'Mark Edwards'),
(24,'Scott Thomas'),
(25,'Roald Dahl'),
(26,'Stephen Jones'),
(27,'Stephen King'),
(28,'Dathan Auerbach'),
(29,'Matt Shaw'),
(30,'Darcy Coates'),
(31,'Madeleine Roux'),
(32,'J-F. Dubeau'),
(33,'Gretchen McNeil'),
(34,'Katie Alender'),
(35,'Jay Anson'),
(36,'Christopher Priest'),
(37,'Micheal McDowell'),
(38,'Bernard Taylor'),
(39,'Adam Nevill'),
(40,'Christopher Motz'),
(41,'Tobias Wade'),
(42,'Lee Mountford'),
(43,'Brian Coldrick'),
(44,'Jeremy Bates'),
(45,'A. Lopez Jr.'),
(46,'Tina Fey'),
(47,'Mindy Kaling'),
(48,'Amy Poehler'),
(49,'David Sedaris'),
(50,'Douglas Adams'),
(51,'Terry Pratchett'),
(52,'Halen Fielding'),
(53,'Jim Gaffigan'),
(54,'Christopher Moore'),
(55,'Graeme Simsion'),
(56,'Jamie McGuire'),
(57,'Jane Austen'),
(58,'Simone Elkeles'),
(59,'Nicholas Sparks'),
(60,'S.C. Stephens'),
(61,'Diana Gabaldon'),
(62,'Sylvia Day'),
(63,'Margaret Mitchell'),
(64,'Pat Conroy'),
(65,'Colleen Hoover'),
(66,'Audry Niffenegger'),
(67,'J.R. Ward'),
(68,'Becca Fitzpatrick'),
(69, 'Frank Herbert'),
(70,'Orson Scott Card'),
(71, 'J.R.R. Tolkien'),
(72, 'Neil Gaiman'),
(73, 'Joe Haldeman'),
(74, 'Max Brooks'),
(75, 'Ursula K. Le Guin'),
(76, 'Philip K. Dick'),
(77, 'Charlie Jane Anders'),
(78, 'Margaret Atwood'),
(79, 'Andy Weir'),
(80, 'Ernest Cline'),
(81, 'Kurt Vonnegut'),
(82, 'Becky Chambers'),
(83, 'Arthur C. Clarke'),
(84, 'Richard M. Powers'),
(85, 'Stanislaw Lem'),
(86, 'Yoon Ha Lee'),
(87, 'Madeleine L Engle'),
(88, 'Kim Stanley Robinson'),
(89, 'Isaac Asimov'),
(90, 'Robert A. Heinlein'),
(91, 'Justine Korman');

INSERT INTO Publisher(publisher_id, publisher) VALUES
(1,'Simon & Schuster'),
(2,'Little'),
(3,'Broadway Books'),
(4,'Penguin Press'),
(5,'Ballantine Books'),
(6,'Back Bay Books'),
(7,'Picador'),
(8,'Ignatius'),
(9,'Dundurn'),
(10,'HarperOne'),
(11,'WW Norton'),
(12,'Portfolio'),
(13,'Fitzhenry and Whiteside'),
(14,'Grove Press'),
(15,'Skyhorse Publishing'),
(16,'Dangerous Books'),
(17,'Henry Holt and Co.'),
(18,'Candlewick'),
(19,'Touchstone'),
(20,'Thomas & Mercer'),
(21,'Inkshares'),
(22,'Farrar, Straus and Giroux'),
(23,'St. Martins Griffin'),
(24,'Scribner'),
(25,'1000Vultures'),
(26,'CreateSpace Independent Publishing Platform'),
(27,'Black Owl Books'),
(28,'HarperCollins'),
(29,'Balzer + Bray'),
(30,'Disney-Hyperion'),
(31,'Pocket Star'),
(32,'Valencourt Books'),
(33,'Ritual Limited'),
(34,'Amazon Digital Services LLC'),
(35,'IDW Publishing'),
(36,'Ghillinnein Books'),
(37,'Ace-Hil-Ink'),
(38,'Reagan Arthur Books'),
(39,'Three Rivers Press'),
(40,'PAN Macmillan'),
(41,'Corgi'),
(42,'William Morrow'),
(43,'Crown Archetype'),
(44,'Atria Books'),
(45,'Dover'),
(46,'Grand Central'),
(47,'Gallery Books'),
(48,'Seal Books'),
(49,'Berkley Trade'),
(50,'Pocket Books'),
(51,'Vintage Canada'),
(52,'Piatkus Books'),
(53, 'Hodder & Stoughton'),
(54, 'Tor Books'),
(55, 'Houghton Mifflin Harcourt'),
(56, 'Open Road Media'),
(57, 'Random House Publishing Group'),
(58, 'Emblem Editions'),
(59, 'The Dial Press'),
(60, 'Stoughton'),
(61, 'Crown'),
(62, 'Rosetta Books'),
(63, 'Pro Auctore Wojciech Zemek'),
(64, 'Solaris'),
(65, 'Orbit'),
(66, 'Spectra'),
(67, 'Amereon'),
(68, 'Disney');

INSERT INTO Genre(genre_id, genre) VALUES
(1,'Biography'),
(2,'Horror'),
(3,'Comedy'),
(4,'Romance'),
(5,'Science Fiction'),
(6,'Fantasy'),
(7,'Non-Fiction'),
(8,'Fiction'),
(9,'Historical'),
(10,'Paranormal'),
(11,'Drama'),
(12, 'Classic'),
(13, 'Utopian'),
(14, 'Dystopian'),
(15, 'Satire'),
(16, 'War Story');

INSERT INTO Book(book_id, isbn, title, description, pub_date, inventory_date, page, list_price,
sale_price, wholesale_price, removed, total_sale, image, publisher_id, format_id) VALUES
(1,'1501139150','Leonardo da Vinci','The author of the acclaimed bestsellers Steve Jobs, Einstein, and Benjamin Franklin brings Leonardo da Vinci to life in this exciting new biography.','2017-10-17','2017-10-28',624,45,16.16,10,0,0,'leonardo_b.jpg',1,1),
(2,'316512583X','Obama: An Intimate Portrait','Obama: An Intimate Portrait reproduces more than 300 of Souzas most iconic photographs with fine-art print quality in an oversize collectible format. Together they document the most consequential hours of the Presidency--including the historic image of President Obama and his advisors in the Situation Room during the bin Laden mission--alongside unguarded moments with the Presidents family, his encounters with children, interactions with world leaders and cultural figures, and more.','2017-11-7','2017-12-9',352,52,18.11,12,0,0,'obama_b.jpg',2,1),
(3,'1400082773','Dreams from My Father: A Story of Race and Inheritance','In this lyrical, unsentimental, and compelling memoir, the son of a black African father and a white American mother searches for a workable meaning to his life as a black American. It begins in New York, where Barack Obama learns that his father—a figure he knows more as a myth than as a man—has been killed in a car accident. This sudden death inspires an emotional odyssey—first to a small town in Kansas, from which he retraces the migration of his mothers family to Hawaii, and then to Kenya, where he meets the African side of his family, confronts the bitter truth of his fathers life, and at last reconciles his divided inheritance.','2004-8-10','2008-2-2',464,21,4.13,7,0,0,'dreams_b.jpg',3,1),
(4,'159420487X','Grant','Ulysses S. Grant life has typically been misunderstood. All too often he is caricatured as a chronic loser and an inept businessman, or as the triumphant but brutal Union general of the Civil War. But these stereotypes do not come close to capturing him, as Chernow shows in his masterful biography, the first to provide a complete understanding of the general and president whose fortunes rose and fell with dizzying speed and frequency.','2017-10-10','2017-11-1',1104,54,14.86,25,0,0,'grant_b.jpg',4,7),
(5,'345350685X','The Autobiography of Malcolm X','With its first great victory in the landmark Supreme Court decision Brown v. Board of Education in 1954, the civil rights movement gained the powerful momentum it needed to sweep forward into its crucial decade, the 1960s. As voices of protest and change rose above the din of history and false promises, one voice sounded more urgently, more passionately, than the rest. Malcolm X—once called the most dangerous man in America—challenged the world to listen and learn the truth as he experienced it. And his enduring message is as relevant today as when he first delivered it.','1987-10-12','2000-1-12',496,9.89,0,1.01,0,0,'malcomx_b.jpg',5,1),
(6,'074325807X','Benjamin Franklin: An American Life','In this authoritative and engrossing full-scale biography, Walter Isaacson, bestselling author of Einstein and Steve Jobs, shows how the most fascinating of America founders helped define our national character','2004-6-1','2006-3-24',608,23.99,6.49,11.32,0,0,'benjamin_b.jpg',1,3),
(7,'316548189X','Long Walk to Freedom: The Autobiography of Nelson Mandela','Nelson Mandela is one of the great moral and political leaders of our time: an international hero whose lifelong dedication to the fight against racial oppression in South Africa won him the Nobel Peace Prize and the presidency of his country. Since his triumphant release in 1990 from more than a quarter-century of imprisonment, Mandela has been at the center of the most compelling and inspiring political drama in the world. As president of the African National Congress and head of South Africas antiapartheid movement, he was instrumental in moving the nation toward multiracial government and majority rule. He is revered everywhere as a vital force in the fight for human rights and racial equality.','1995-10-1','1996-1-5',656,23.27,0,10.11,0,0,'longwalk_b.jpg',6,5),
(8,'1250159938','Coretta: My Life, My Love, My Legacy','[Coretta: My Life, My Love, My Legacy] "reveals never-before-told aspects of Mrs. King life....We learn of the brilliant mind and courageous spirit behind the enigmatic figure” (Essence)','2018-1-20','2018-1-21',384,22.28,0,14.12,0,0,'correta_b.jpg',7,1),
(9,'898709857X','The Message of St. Francis','This is a celebration of the life of St. Francis, great lover of God, man and nature. Extracts from his writings and from biographies of the saint are selected by Sister Nan, a member of the Community of St. Francis in London. Illustrated with stunning details and images from the fresco-cycle in the Basilica at Assisi, the book invites the reader to step into the world of St. Francis for a unique spiritual experience.','2003-7-1','2003-7-11',57,19.99,2,5.89,0,0,'themessage_b.jpg',8,1),
(10,'1459737237','The Man Who Carried Cash: Saul Holiff, Johnny Cash, and the Making of an American Icon','Before there was Johnny and June, there was Johnny and Saul. The Man Who Carried Cash chronicles a relationship that was both volatile and affectionate between Johnny Cash and his manager, Saul Holiff. From roadside taverns to the roaring crowds at Madison Square Garden, from wrecked cars and jail cells all the way to the White House, the story of Johnny and Saul is a portrait of two men from different worlds who were more alike than either cared to admit.','2017-5-27','2018-1-1',392,19.99,0,8.89,0,0,'carriedcash_b.jpg',9,1),
(11,'60750499X','Keplers Witch: An Astronomers Discovery of Cosmic Order Amid Religious War, Political Intrigue, and the Heresy Trial of His Mother','Set against the backdrop of the witchcraft trial of his mother, this lively biography of Johannes Kepler – the Protestant Galileo and 16th century mathematician and astronomer – reveals the surprisingly spiritual nature of the quest of early modern science.','2005-5-10','2007-3-15',416,53.95,13.04,15.69,0,0,'kelper_b.jpg',10,7),
(12,'393647013X','Gorbachev: His Life and Times','When Mikhail Gorbachev became the leader of the Soviet Union in 1985, the USSR was one of the world’s two superpowers. By 1989, his liberal policies of perestroika and glasnost had permanently transformed Soviet Communism, and had made enemies of radicals on the right and left.','2017-9-5','2017-12-9',928,36,4.43,21.11,0,0,'gorbachev_b.jpg',11,1),
(13,'1591846404','Turn the Ship Around ! A True Story of Turning Followers Into Leaders','Since Turn the Ship Around! was published in 2013, hundreds of thousands of readers have been inspired by former Navy captain David Marquet’s true story. Many have applied his insights to their own organizations, creating workplaces where everyone takes responsibility for his or her actions, where followers grow to become leaders, and where happier teams drive dramatically better results.','2017-9-5','2018-1-15',928,16,1.6,3.33,0,0,'shiparound_b.jpg',12,1),
(14,'1554551838','Gandhi: An Autobiography, The Story of My Experiments with Truth','Mohandas K Gandhi is one of the most inspiring figures of our time. In his classic autobiography he recounts the story of his life and how he developed his concept of active nonviolent resistance, which propelled the Indian struggle for independence and countless other nonviolent struggles of the twentieth century.','2013-5-16','2016-12-12',272,29.5,8.39,14.25,0,0,'gandhiautho_b.jpg',13,5),
(15,'080214411X','Che Guevara: A Revolutionary Life','Acclaimed around the world and a national best-seller, this is the definitive work on Che Guevara, the dashing rebel whose epic dream was to end poverty and injustice in Latin America and the developing world through armed revolution. Jon Lee Anderson biography traces Che extraordinary life, from his comfortable Argentine upbringing to the battlefields of the Cuban revolution, from the halls of power in Castro government to his failed campaign in the Congo and assassination in the Bolivian jungle.','2011-2-24','2015-6-8',542,51.5,9.63,2.56,0,0,'cheguevara_b.jpg',14,1),
(16,'1616086009','The General: Charles De Gaulle and the France He Saved','No leader of modern times was more uniquely national than Charles de Gaulle. In his twenties, he fought for France in the trenches and at the epic battle of Verdun. In the 1930s, he waged a lonely battle to enable France better to resist Hitler’s Germany. Thereafter, he twice rescued the nation from defeat and decline by extraordinary displays of leadership, political acumen, daring, and bluff, heading off civil war and leaving a heritage adopted by his successors of right and left.','2010-4-30','2015-5-25',672,20.66,0,9.15,0,0,'charlesgaulle_b.jpg',15,1),
(17,'446676500X','The Autobiography of Martin Luther King, Jr.','Using Stanford University voluminous collection of archival material, including previously unpublished writings, interviews, recordings, and correspondence, King scholar Clayborne Carson has constructed a remarkable first-person account of Dr. King extraordinary life.','2012-7-1','2016-9-12',736,37.54,0,7.54,0,0,'martinluther_b.jpg',13,1),
(18,'1947979000','Fatwa: Hunted in America','If this book is proof of anything, it that one person can make a difference. And what a remarkable difference Pamela Geller has made. At last, in Fatwa: Hunted In America, she tells her story.','2001-1-1','2001-1-1',416,21.99,2.65,9.58,0,0,'fatwa_b.jpg',16,3),
(19,'1451695195','Proof of Heaven: A Neurosurgeon Journey into the Afterlife','Thousands of people have had near-death experiences, but scientists have argued that they are impossible. Dr. Eben Alexander was one of those scientists. A highly trained neurosurgeon, Alexander knew that NDEs feel real, but are simply fantasies produced by brains under extreme stress.','2014-11-1','2017-11-12',110,34.5,4.24,15.58,0,0,'neurosurgeon_b.jpg',1,1),
(20,'805099689X','Petty: The Biography','No one other than Warren Zanes, rocker and writer and friend, could author a book about Tom Petty that is as honest and evocative of Petty music and the remarkable rock and roll history he and his band helped to write.','2012-10-23','2018-1-15',208,18.81,0,3.59,0,0,'petty_b.jpg',17,1),
(21,'076367236X','Book: My Autobiography','Books are one of humankind greatest forms of expression, and now Book, in a witty, idiosyncratic voice, tells us the inside story. A wonderfully eccentric character with strong opinions and a poetic turn of phrase, Book tells of a journey from papyrus scrolls to medieval manuscripts to printed paper and beyond—pondering, along the way, many bookish things, including the evolution of the alphabet, the library (known to Egyptians as "the healing place of the soul"), and even book burning. With bold, black-and-white illustrations by Neil Packer, Book is a captivating work of nonfiction by one of England leading poets.','2015-11-10','2018-1-26',336,22.99,2.3,3.33,0,0,'myauto_b.jpg',18,1),
(22,'1451662440','Total Recall: My Unbelievably True Life Story','In his signature larger-than-life style, Arnold Schwarzenegger Total Recall is a revealing self-portrait of his illustrious, controversial, and truly unique life.','2015-10-6','2015-10-7',144,35,10.47,9.98,0,0,'totalrecall_b.jpg',1,3),
(23,'316194751X','Johnny Cash: The Life','In this, the definitive biography of an American legend, Robert Hilburn conveys the unvarnished truth about a musical superstar. Johnny Cash extraordinary career stretched from his days at Sun Records with Elvis Presley and Jerry Lee Lewis to the remarkable creative last hurrah, at age 69, that resulted in the brave, moving "Hurt" video.','2013-11-5','2016-11-5',688,19.79,0,9.57,0,0,'johnnycash_b.jpg',2,1),
(24,'671705725X','Real Frank Zappa Book','Recounts the life and career of the inventive and controversial rock musician, and includes information on his philosophies on art, his opinions on the music industry, and his thoughts on raising children.','1990-5-15','2018-1-1',352,19.79,0,9.57,0,0,'frankzappa_b.jpg',19,1),
(25,'307388409X','Open: An Autobiography','Far more than a superb memoir about the highest levels of professional tennis, Open is the engrossing story of a remarkable life.','2010-8-10','2012-10-10',400,23,4.7,1.24,0,0,'open_b.jpg',20,1),
(26,'1503944379', 'Follow You Home', 'The page-turning psychological thriller from the author of #1 bestsellers The Magpies and Because She Loves Me.  It was supposed to be the trip of a lifetime, a final adventure before settling down. But after a perfect start, an encounter with a young couple on a night train forces Daniel and Laura to cut their dream trip short and flee home.  Back in London, Daniel and Laura vow never to talk about what happened that night. But as they try to fit into their old lives again, they realise they are in terrible danger—and that their nightmare is just beginning…','2015-6-30','2017-7-14',381,7.99,0,2.50,0,0,'followyouhome_b.jpg',20,4),
(27,'1942645821','Kill Creek','At the end of a dark prairie road, nearly forgotten in the Kansas countryside, is the Finch House. For years it has remained empty, overgrown, abandoned. Soon the door will be opened for the first time in decades. But something is waiting, lurking in the shadows, anxious to meet its new guests…','2017-10-31','2017-11-25',432, 12.76,0,4,0,0,'killcreek_b.jpg',21,1),
(28,'0374518688','Roald Dahls Book of Ghost Stories','Who better to investigate the literary spirit world than that supreme connoisseur of the unexpected, Roald Dahl? Of the many permutations of the macabre or bizarre, Dahl was always especially fascinated by the classic ghost story. As he realtes in the erudite introduction to this volume, he read some 749 supernatural tales at the British Museum Library before selecting the 14 that comprise this anthology. "Spookiness is, after all, the real purpose of the ghost story," Dahl writes. "It should give you the creeps and disturb your thoughts." For this superbly disquieting collection, Dahl offers favorite tales by such masterful storytellers as E. F. Benson, J. Sheridan Le Fanu, Rosemary Timperley, and Edith Wharton.','1984-10-1','2015-10-31',235,5.64,0,1.50,0,0,'roalddahlsbookofghoststories_b.jpg',22,7),
(29,'9781250018','A Book of Horrors','A collection of original horror and dark fantasy from the worlds best writers, including Stephen King and John Ajvide Lindqvist.  Many of us grew up on The Pan Book of Horror Stories and its later incarnations, Dark Voices and Dark Terrors (The Gollancz Book of Horror), which won the World Fantasy Award, the Horror Critics Guild Award and the British Fantasy Award, but for a decade or more there has been no non-themed anthology of original horror fiction published in the mainstream. Now that horror has returned to the bookshelves, it is time for a regular anthology of brand-new fiction by the best and brightest in the field, both the Big Names and the most talented newcomers.','2012-9-18','2016-3-20',448,12.38,0,3.44,0,0,'abookofhorrors_b.jpg',23,5),
(30,'1501182099','IT','Stephen Kings terrifying, classic #1 New York Times bestseller, a landmark in American literature (Chicago Sun-Times)—about seven adults who return to their hometown to confront a nightmare they had first stumbled on as teenagers…an evil without a name: It.  Welcome to Derry, Maine. Its a small city, a place as hauntingly familiar as your own hometown. Only in Derry the haunting is real. They were seven teenagers when they first stumbled upon the horror. Now they are grown-up men and women who have gone out into the big world to gain success and happiness. But the promise they made twenty-eight years ago calls them reunite in the same place where, as teenagers, they battled an evil creature that preyed on the city’s children. Now, children are being murdered again and their repressed memories of that terrifying summer return as they prepare to once again battle the monster lurking in Derrys sewers. Readers of Stephen King know that Derry, Maine, is a place with a deep, dark hold on the author. It reappears in many of his books, including Bag of Bones, Hearts in Atlantis, and 11/22/63. But it all starts with It.Stephen Kings most mature work (St. Petersburg Times), It will overwhelm you… to be read in a well-lit room only','2016-1-1','2017-7-4',1169,12.73,0,6.50,0,0,'it_b.jpg',24,6),
(31,'098554550X','Penpal','Penpal began as a series of short and interconnected stories posted on an online horror forum. Before long, it was adapted into illustrations, audio recordings, and short films; and that was before it was revised and expanded into a novel! How much do you remember about your childhood?  In Penpal, a man investigates the seemingly unrelated bizarre, tragic, and horrific occurrences of his childhood in an attempt to finally understand them. Beginning with only fragments of his earliest years, youll follow the narrator as he discovers that these strange and horrible events are actually part of a single terrifying story that has shaped the entirety of his life and the lives of those around him. If youve ever stayed in the woods just a little too long after dark, if youve ever had the feeling that someone or something was trying to hurt you, if you remember the first friend you ever made and how strong that bond was, then Penpal is a story that you wont soon forget, despite how you might try.','2012-7-11','2016-6-12',252,7.22,0,2.25,0,0,'penpal_b.jpg',25,2),
(32,'1499772858','sick b*stards','WARNING: THIS IS AN EXTREME HORROR NOVEL. There is gore. There is bad language. There are scenes of a sexual nature. But hidden underneath it all is also a chilling story. Please do not purchase this book if you are easily shocked, disgusted or offended. This book is not for you. A family will do anything to survive after a nuclear attack has left their world in ruins. Actions which even surprise them... Although a stand-alone tale - for those of you who enjoyed the story of Sick Bastards, there is a second one in the series available here: smarturl.it/sickERbastards','2014-6-10','2016-8-11',236,7.50,0,3.00,0,0,'sickbastards_b.jpg',26,3),
(33,'0994630603','The Haunting of Ashburn House','The ancient building has been the subject of rumours for close to a century. Its owner, Edith, refused to let guests inside and rarely visited the nearby town. Following Ediths death, her sole surviving relative, Adrienne, inherits the property. Adriennes only possessions are a suitcase of luggage, twenty dollars, and her pet cat. Ashburn House is a lifeline she cant afford to refuse. Adrienne doesnt believe in ghosts, but its hard to ignore the unease that grows as she explores her new home. Strange messages have been etched into the wallpaper, an old grave is hidden in the forest behind the house, and eerie portraits in the upstairs hall seem to watch her every movement. As she uncovers more of the houses secrets, Adrienne begins to believe the whispered rumours about Ashburn may hold more truth than she ever suspected. The building has a bleak and grisly past, and as she chases the threads of a decades-old mystery, Adrienne realises shes become the prey to something deeply unnatural and intensely resentful. Only one thing is certain: Ashburns dead are not at rest.','2016-8-14','2017-1-25',342,15.99,0,10.67,0,0,'thehauntingofashburnhouse_b.jpg',27,6),
(34,'0062574337','Asylum 3-Book Box Set:Asylum, Sanctum, Catacomb','Sometimes the past is better off buried… Enter the twisted world of Madeleine Roux’s New York Timesbestselling Asylum series with this bone-chilling box set containing the first three novels. In Asylum, sixteen-year-old Dan discovers that his summer-program dorm used to be a psychiatric hospital—and that its filled with secrets linking Dan and his new friends to the asylums dark past. In Sanctum, when Dan, Abby, and Jordan receive anonymous photos of an old carnival inviting them back to the asylum, they return to end the nightmare once and for all. In Catacomb, the three friends embark on a senior road trip to New Orleans, but with a mysterious group known as the Bone Artists on their trail, they will be lucky to make it out of the trip alive… Featuring found photographs from real asylums and filled with chilling mystery and page-turning suspense, the Asylum series treads the line between past and present, genius and insanity.','2016-9-6','2018-1-1',1056,17.51,0,8.99,0,0,'asylum_b.jpg',28,3),
(35,'194264535X','A God in the Shed','The village of Saint-Ferdinand has all the trappings of a quiet life: farmhouses stretching from one main street, a small police precinct, a few diners and cafés, and a grocery store. Though if an out-of-towner stopped in, they would notice one unusual thing―a cemetery far too large and much too full for such a small town, lined with the victims of the Saint-Ferdinand Killer, who has eluded police for nearly two decades. It’s not until after Inspector Stephen Crowley finally catches the killer that the town discovers even darker forces are at play. When a dark spirit reveals itself to Venus McKenzie, one of Saint-Ferdinand’s teenage residents, she learns that this creature’s power has a long history with her town―and that the serial murders merely scratch the surface of a past burdened by evil secrets.','2017-6-13','2018-1-22',450,13.16,0,4.14,0,0,'agodintheshed_b.jpg',21,4),
(36,'006211879X','TEN','Ten teens. Three days. One killer. It was supposed to be the weekend of their lives—an exclusive house party on Henry Island. Best friends Meg and Minnie are looking forward to two days of boys, booze, and fun-filled luxury. But what starts out as fun turns twisted after the discovery of a DVD with a sinister message: Vengeance is mine. And things only get worse from there. With a storm raging outside, the teens are cut off from the outside world . . . so when a mysterious killer begins picking them off one by one, there’s no escape. As the deaths become more violent and the teens turn on one another, can Meg find the killer before more people die? Or is the killer closer to her than she could ever imagine? Perfect for fans of Christopher Pike’s Chain Letter and Lois Duncan’s I Know What You Did Last Summer, Ten will keep readers on the edge of their seats until the very last page!','2013-9-17','2017-6-5',320,5.49,0,1.50,0,0,'ten_b.jpg',28,7),
(37,'1501166115','The Dark Tower I:The Gunslinger','“An impressive work of mythic magnitude that may turn out to be Stephen King’s greatest literary achievement” (The Atlanta Journal-Constitution), The Gunslinger is the first volume in the epic Dark Tower Series. A #1 national bestseller, The Gunslinger introduces readers to one of Stephen King’s most powerful creations, Roland of Gilead: The Last Gunslinger. He is a haunting figure, a loner on a spellbinding journey into good and evil. In his desolate world, which mirrors our own in frightening ways, Roland tracks The Man in Black, encounters an enticing woman named Alice, and begins a friendship with the boy from New York named Jake. Inspired in part by the Robert Browning narrative poem, “Childe Roland to the Dark Tower Came,” The Gunslinger is “a compelling whirlpool of a story that draws one irretrievable to its center” (Milwaukee Sentinel). It is “brilliant and fresh…and will leave you panting for more” (Booklist).','2016-1-1','2018-1-23',385,9.48,0,3.33,0,0,'thedarktowerithegunslinger_b.jpg',24,4),
(38,'1423108779','Bad Girls Dont Die','When Alexiss little sister Kasey becomes obsessed with an antique doll, Alexis thinks she is just being her usual weird self. Things get weirder, though, when their house starts changing. Doors open and close by themselves; water boils on the unlit stove; and an unplugged air conditioner blasts cold air. Kasey is changing, too. Her blue eyes go green, she starts using old-fashioned language, and she forgets chunks of time. Most disturbing of all is the dangerous new chip on Kaseys shoulder. The formerly gentle child is gone, and the new Kasey is angry. Alexis is the only one who can stop her sister -- but what if that green-eyed girl isnt even Kasey anymore?','2010-6-22','2017-12-22',352,6.40,0,1.25,0,0,'badgirlsdontdie_b.jpg',30,1),
(39,'0062220974','Asylum','Madeleine Rouxs New York Times bestselling Asylum is a thrilling and creepy photo-illustrated novel that Publishers Weekly called "a strong YA debut that reveals the enduring impact of buried trauma on a place." Featuring found photographs from real asylums and filled with chilling mystery and page-turning suspense, Asylum is a horror story that treads the line between genius and insanity, perfect for fans of Miss Peregrines Home for Peculiar Children. For sixteen-year-old Dan Crawford, the New Hampshire College Prep program is the chance of a lifetime. Except that when Dan arrives, he finds that the usual summer housing has been closed, forcing students to stay in the crumbling Brookline Dorm—formerly a psychiatric hospital. As Dan and his new friends Abby and Jordan start exploring Brooklines twisty halls and hidden basement, they uncover disturbing secrets about what really went on here... secrets that link Dan and his friends to the asylums dark past. Because Brookline was no ordinary mental hospital, and there are some secrets that refuse to stay buried.','2016-8-26','2018-1-20',336,8.31,0,4.41,0,0,'asylum2_b.jpg',28,2),
(40,'1416507698','The Amityville Horror','Provides a chilling account account of the four weeks of terror experienced by an Amityville, Long Island, family after moving into a house in which a particularly gruesome mass murder had once been committed.','2005-8-5','2017-10-10',256,7.19,0,3.89,0,0,'theamityvillehorror_b.jpg',31,2),
(41,'1943910510','The Valencourt Book of Horror Stories','Spanning two hundred years of horror, this new collection features seventeen macabre gems, including two original tales and many others that have never or seldom been reprinted, by: Charles Birkin • John Blackburn • Michael Blumlein • Mary Cholmondeley • Hugh Fleetwood • Stephen Gregory • Gerald Kersh • Francis King • M. G. Lewis • Florence Marryat • Richard Marsh • Michael McDowell • Christopher Priest • Forrest Reid • Bernard Taylor • Hugh Walpole','2016-10-4','2017-6-22',278,15.99,0,8.99,0,0,'thevalencourtbookofhorrorstories_b.jpg',32,5),
(42,'0995463034','Some Will Not Sleep:Selected Horrors','A bestial face appears at windows in the night. In the big white house on the hill angels are said to appear. A forgotten tenant in an isolated building becomes addicted to milk. A strange goddess is worshipped by a home-invading disciple. The least remembered gods still haunt the oldest forests. Cannibalism occurs in high society at the end of the world. The sainted undead follow their prophet to the Great Dead Sea. A confused and vengeful presence occupies the home of a first-time buyer...','2016-10-31','2017-10-1',292,15.31,0,7.50,0,0,'somewillnotsleep_b.jpg',33,3),
(43,'154678926X','Pine Lakes','Ted and Susan Merchant have been coming to the Pine Lakes Resort for nearly twenty years. In that time, their annual getaway has become a tradition, a peaceful week nestled in the picturesque mountains of northern Pennsylvania. For them, it isn’t just a simple vacation spot, but a magical destination where time slows and they become one with nature’s magnificence. When a freak summer storm forces Ted to lose control of their car and plummet off the road, they’re relieved just to have survived, but relief quickly turns to dread as they find out all too soon they’re not alone. Strange lights in the forest, whispered warnings, the insurmountable threat of unseen forces lurking in the shadows; nothing could have prepared them for the horrors that lie ahead. Escaping the wreck is just the beginning. Is love enough to save them, or will they become the latest victims on the road to Pine Lakes?','2017-6-9','2018-1-20',333,13.95,0,4.75,0,0,'pinelakes_b.jpg',34,4),
(44,'1979340471','Brutal Bedtime Stories:A Supernatural Horror Collection','Multiple Award Winning Authors from Reddits 12 million subscriber /Nosleep WARNING: Contains mature and graphic material. Not for the faint of heart Spine-tingling terror from horror writers around the world. Dozens of diverse short stories containing gruesome murders, supernatural mysteries, grotesque hellscapes, and deranged psychopaths to keep you up at night. Surprise twists ensure you keep guessing until the last page. Special Edition with Full page original illustrations!','2017-10-30','2018-1-20',332,16.99,0,10.99,0,0,'brutalbedtimestories_b.jpg',26,2),
(45,'1974287734','Horror In The Woods','FINDING THAT DESECRATED BODY WAS ONLY THE BEGINNING... For Ashley and her three friends, it was supposed to be an adventure-filled weekend. A chance to get away from the hustle-and-bustle of city life, and experience the peaceful tranquility of nature. But when they ventured into those woods, their trip turned into a horror far beyond what they could have ever imagined. Because these four friends have wandered into the territory of the violent, grotesque Webb family. A group of psychopaths who have a taste for human meat. And they are hungry! Ashley and her friends must face this evil head on, and worse, discover the shocking secret behind the familys existence... In the vein of THE EVIL DEAD, TEXAS CHAINSAW MASSACRE, and WRONG TURN - HORROR IN THE WOODS will leave you exhausted and drained. A brutal, violent tale that hurtles along at break-neck pace - one that horror fans should not miss!','2017-8-4','2017-12-10',292,13.95,0,5.95,0,0,'horrorinthewoods_b.jpg',26,5),
(46,'0994630638','Craven Manor','Daniel is desperate for a job. When someone slides a note under his door offering him the groundskeeper’s position at an old estate, it seems too good to be true. Alarm bells start ringing when he arrives at Craven Manor. The mansion’s front door hangs open, and leaves and cobwebs coat the marble foyer. It’s clear no one has lived there in a long time. But an envelope waits for him inside the doorway. It contains money, and promises more. Daniel is desperate. Against his better judgement, he moves into the groundskeeper’s cottage behind the crypt. He’s determined to ignore the strange occurrences that plague the estate. But when a candle flickers to life in the abandoned tower window, Daniel realises Craven Manor is hiding a terrible secret… one that threatens to bury him with it.','2017-12-18','2018-1-18',298,15.99,0,6.78,0,0,'cravenmanor_b.jpg',27,7),
(47,'1631409530','Behind You:One-Shot Horror Stories','A twisted, crawling figure. A giggling crowd of masked watchers. A reassembling corpse. Theres something there, just waiting for you to turn around. Behind You is an illustration series, a comic with no panels, where each piece is a separate story. Each tale is one image and one piece of text: an unsuspecting victim with someone, or something, behind them. Entries range from the amusingly weird to the genuinely unsettling. Inspired by spooky films, books, myths, and internet tall tales, Behind You is full of scary set-ups but leaves lots of blanks for readers to fill in with their own narrative. Its the Far Side for horror fans.','2017-10-31','2018-1-20',172,11.38,0,4.55,0,0,'behindyouoneshothorrorstories_b.jpg',35,4),
(48,'0993764622','Suicide Forest','Just outside of Tokyo lies Aokigahara, a vast forest and one of the most beautiful wilderness areas in Japan...and also the most infamous spot to commit suicide in the world. Legend has it that the spirits of those many suicides are still roaming, haunting deep in the ancient woods. When bad weather prevents a group of friends from climbing neighboring Mt. Fuji, they decide to spend the night camping in Aokigahara. But they get more than they bargained for when one of them is found hanged in the morning—and they realize there might be some truth to the legends after all.','2014-12-16','2017-1-24',478,13.95,0,5.96,0,0,'suicideforest_b.jpg',36,6),
(49,'0615787975','Floor Four','A loud bang was heard from above, scaring the boys. They shined their lights up the stairwell. The sounds of chains rattling on the floor stirred the dust above. Something or someone was up there. Doug, hiding his fear, took a couple of more steps up. Brandon and Kyle looked at him, their feet locked in place. They had no intention of going any farther. The old, abandoned Saint Vincent Hospital is said to be haunted by the ghost of David Henry Coleman, the notorious serial killer, known as The Mangler. Coleman died on the fourth floor after being shot by police. For the three Jr. High boys, their curiosity gets the best of them as they explore the old hospital, despite Old Man Jakes warning. No one knew of Jakes dark connection to the killer and the hospital. And now, on the anniversary of The Manglers death, a group of high school kids are planning a private party on the haunted fourth floor. Jake must keep everyone out and protect them from the true evil that lurks on Floor Four.','2013-5-15','2017-11-19',116,4.99,0,0.99,0,0,'floorfour_b.jpg',37,2),
(50,'150116340X','Sleeping Beauties:A Novel','In a future so real and near it might be now, something happens when women go to sleep: they become shrouded in a cocoon-like gauze. If they are awakened, if the gauze wrapping their bodies is disturbed or violated, the women become feral and spectacularly violent. And while they sleep they go to another place, a better place, where harmony prevails and conflict is rare. One woman, the mysterious “Eve Black,” is immune to the blessing or curse of the sleeping disease. Is Eve a medical anomaly to be studied? Or is she a demon who must be slain? Abandoned, left to their increasingly primal urges, the men divide into warring factions, some wanting to kill Eve, some to save her. Others exploit the chaos to wreak their own vengeance on new enemies. All turn to violence in a suddenly all-male world.','2017-10-27','2018-1-22',720,19.85,0,9.99,0,0,'sleepingbeautyanovel_b.jpg',24,4),
(51,'0316056863','Bossypants','Before Liz Lemon, before "Weekend Update," before "Sarah Palin," Tina Fey was just a young girl with a dream: a recurring stress dream that she was being chased through a local airport by her middle-school gym teacher. She also had a dream that one day she would be a comedian on TV.She has seen both these dreams come true. At last, Tina Feys story can be told. From her youthful days as a vicious nerd to her tour of duty on Saturday Night Live; from her passionately halfhearted pursuit of physical beauty to her life as a mother eating things off the floor; from her one-sided college romance to her nearly fatal honeymoon -- from the beginning of this paragraph to this final sentence. Tina Fey reveals all, and proves what we have all suspected: you are no one until someone calls you bossy.','2011-4-05','2018-01-19',283,21.00,09.04,11.96,0,0,'bossypants.jpg',38,8),
(52,'0307886271','Is Everyone Hanging Out Without Me? (And Other Concerns)','Mindy Kaling has lived many lives: the obedient child of immigrant professionals, a timid chubster afraid of her own bike, a Ben Affleck–impersonating Off-Broadway performer and playwright, and, finally, a comedy writer and actress prone to starting fights with her friends and coworkers with the sentence “Can I just say one last thing about this, and then I swear I’ll shut up about it?” Perhaps you want to know what Mindy thinks makes a great best friend (someone who will fill your prescription in the middle of the night), or what makes a great guy (one who is aware of all elderly people in any room at any time and acts accordingly), or what is the perfect amount of fame (so famous you can never get convicted of murder in a court of law), or how to maintain a trim figure (you will not find that information in these pages).','2012-09-18','2018-01-03',242,17.00,03.74,13.76,0,0,'IEHOWM.jpg',39,2),
(53,'1443424536','Yes Please','In a perfect world . . . We’d get to hang out with Amy Poehler, watching dumb movies, listening to music, and swapping tales about our coworkers and difficult childhoods. Unfortunately, between her Golden Globe–winning role on Parks and Recreation, work as a producer and director, place as one of the most beloved SNL alumni and cofounder of the Upright Citizens Brigade, involvement with the website Smart Girls at the Party, frequent turns as acting double for Meryl Streep, and her other gig as the mom of two young sons, she’s not available for movie night. Luckily, we have the next best thing: Yes Please, Amy Poehler’s hilarious and candid book. A collection of stories, thoughts, ideas, lists, and haiku, Yes Please took the world by storm, going straight to #1 on the New York Times bestseller list and dominating lists on both sides of the border for over 17 weeks—including multiple weeks at #1 on the Globe and Mail bestseller list. Widely acclaimed as one of the best books of the year, Yes Please cemented Amy Poehler’s place in our hearts as one of our most beloved entertainers, and in our minds as a sharp, insightful, and provocative writer.','2015-09-01','2017-10-09',352,19.99,0,11.99,0,0,'yesplease.jpg',28,8),
(54,'0316776963','Me Talk Pretty One Day','David Sedaris move to Paris from New York inspired these hilarious pieces, including the title essay, about his attempts to learn French from a sadistic teacher who declares that every day spent with you is like having a caesarean section. His family is another inspiration. You Cant Kill the Rooster is a portrait of his brother, who talks incessant hip-hop slang to his bewildered father. And no one hones a finer fury in response to such modern annoyances as restaurant meals presented in ludicrous towers of food and cashiers with six-inch fingernails.','2001-06-05','2017-01-17',288,21.00,0,12.99,0,0,'MTPOD.jpg',6,8),
(55,'0330492047','The Hitchhikers Guide To The Galaxy: The Trilogy Of Four','THE HITCHIKERS GUIDE TO THE GALAXY: One Thursday lunchtime the Earth gets demolished to make way for a hyperspace bypass. For Arthur, who has just had his house demolished, this is too much. Sadly, the weekends just begun. THE RESTAURANT AT THE END OF THE UNIVERSE: When all issues of space, time, matter and the nature of being are resolved, only one question remains: Where shall we have dinner? The Restaurant at the End of the Universe provides the ultimate gastronomic experience and, for once, there is no morning after. LIFE, THE UNIVERSE AND EVERYTHING: In consequence of a number of stunning catastrophes,Arthur Dent is surprised to find himself living in a hideously miserable cave on prehistoric Earth. And then, just as he thinks that things cannot possibly get any worse, they suddenly do. SO LONG, AND THANKS FOR ALL THE FISH: Arthur Dents sense of reality is in its dickiest state when he suddenly finds the girl of his dreams. They go in search of Gods Final Message and, in a dramatic break with tradition, actually find it.','2002-03-08','2017-10-11',720,19.99,0,17.55,0,0,'THGTTG.jpg',40,8),
(56,'0552124753','The Color of Magic','On a world supported on the back of a giant turtle (sex unknown), a gleeful, explosive, wickedly eccentric expedition sets out.  Theres an avaricious but inept wizard, a naive tourist whose luggage moves on hundreds of dear little legs, dragons who only exist if you believe in them, and of course THE EDGE of the planet...','1990-04-01','2017-08-19',288,13.99,2.21,11.78,0,0,'TheColorofMagic.jpg',41,8),
(57,'0552131067','Mort','Death comes to us all.  When he came to Mort, he offered him a job.After being assured that being dead was not compulsory, Mort accepted.  However, he soon found that romantic longings did not mix easily with the responsibilities of being Deaths apprentice...','1989-04-01','2017-04-21',272,13.99,0,10.99,0,0,'Mort.jpg',41,8),
(58,'1447288939','Bridget Jones Diary','A dazzlingly urban satire on modern relationships?An ironic, tragic insight into the demise of the nuclear family?Or the confused ramblings of a pissed thirty-something?Welcome to Bridgets first diary: mercilessly funny, endlessly touching and utterly addictive. ','2014-11-06','2017-09-04',320,18.99,4.99,9.99,0,0,'BridgetJonesDiary.jpg',40,8),
(59,'0385349076','Dad Is Fat','Though he grew up in a large Irish-Catholic family, Jim was satisfied with the nomadic, nocturnal life of a standup comedian, and was content to be "that weird uncle who lives in an apartment by himself in New York that everyone in the family speculates about." But all that changed when he married and found out his wife, Jeannie "is someone who gets pregnant looking at babies." Five kids later, the comedian whose riffs on everything from Hot Pockets to Jesus have scored millions of hits on YouTube, started to tweet about the mistakes and victories of his life as a dad. Those tweets struck such a chord that he soon passed the million followers mark. But it turns out 140 characters are not enough to express all the joys and horrors of life with five kids, so hes now sharing it all in Dad Is Fat.','2014-04-22','2018-01-22',288,18.99,0,9.99,0,0,'DadIsFat.jpg',39,8),
(60,'0060590289','A Dirty Job','Charlie Asher is a pretty normal guy with a normal life, married to a bright and pretty woman who actually loves him for his normalcy. Theyre even about to have their first child. Yes, Charlies doing okay—until people start dropping dead around him, and everywhere he goes a dark presence whispers to him from under the streets. Charlie Asher, it seems, has been recruited for a new position: as Death. Its a dirty job. But, hey! Somebodys gotta do it.','2007-03-27','2017-01-16',416,18.50,0,11.99,0,0,'ADirtyJob.jpg',42,8),
(61,'0804138141','Why Not Me?','In Why Not Me?, Kaling shares her ongoing journey to find contentment and excitement in her adult life, whether it’s falling in love at work, seeking new friendships in lonely places, attempting to be the first person in history to lose weightwithout any behavior modification whatsoever, or most important, believing that you have a place in Hollywood when you’re constantly reminded that no one looks like you','2015-09-15','2017-10-30',240,22.00,4.99,13.99,0,0,'WhyNotMe.jpg',43,8),
(62,'1443422665','The Rosie Project','A first-date dud, socially awkward and overly fond of quick-dry clothes, genetics professor Don Tillman has given up on love, until a chance encounter gives him an idea.He will design a questionnaire—a sixteen-page, scientifically researched questionnaire—to uncover the perfect partner. She will most definitely not be a barmaid, a smoker, a drinker or a late-arriver. Rosie is all these things. She is also fiery and intelligent, strangely beguiling, and looking for her biological father a search that a DNA expert might just be able to help her with.The Rosie Project is a romantic comedy like no other. It is arrestingly endearing and entirely unconventional, and it will make you want to drink cocktails.','2013-05-21','2017-05-11',320,12.50,0,8.99,0,0,'TheRosieProject.jpg',28,8),
(63,'1476712042','Beautiful Disaster','The new Abby Abernathy is a good girl. She doesn’t drink or swear, and she has the appropriate number of cardigans in her wardrobe. Abby believes she has enough distance from the darkness of her past, but when she arrives at college withher best friend, her path to a new beginning is quickly challenged by Eastern University’s Walking One-Night Stand. Travis Maddox, lean, cut, and covered in tattoos, is exactly what Abby wants—and needs—to avoid. He spends his nights winning money in a floating fight ring, and his days as the ultimate college campus charmer. Intrigued by Abby’s resistance to his appeal, Travis tricks her into his daily life with a simple bet. If he loses, he must remain abstinent for a month. If Abby loses, she must live in Travis’s apartment for the same amount of time. Either way, Travis has no idea that he has met his match.','2012-08-14','2017-02-17',432,22.00,6.99,5.99,0,0,'BeautifulDisaster.jpg',44,8),
(64,'0486284735','Pride and Prejudice','One of the most universally loved and admired English novels, Pride and Prejudice was penned as a popular entertainment. But the consummate artistry of Jane Austen (1775–1817) transformed this effervescent tale of rural romance into a witty, shrewdly observed satire of English country life that is now regarded as one of the principal treasures of English language.In a remote Hertfordshire village, far off the good coach roads of George IIIs England, a country squire of no great means must marry off his five vivacious daughters. At the heart of this all-consuming enterprise are his headstrong second daughter Elizabeth Bennet and her aristocratic suitor Fitzwilliam Darcy — two lovers whose pride must be humbled and prejudices dissolved before the novel can come to its splendid conclusion.','1995-04-12','2017-01-30',272,4.99,0,0.99,0,0,'PrideandPrejudice.jpg',45,8),
(65,'1847388051','Perfect Chemistry','When Brittany Ellis walks into chemistry class on the first day of senior year, she has no clue that her carefully created "perfect" life is about to unravel before her eyes. Shes forced to be lab partners with Alex Fuentes, a gang member from the other side of town, and he is about to threaten everything shes worked so hard for-her flawless reputation, her relationship with her boyfriend, and the secret that her home life is anything but perfect. Alex is a bad boy and he knows it. So when he makes a bet with his friends to lure Brittany into his life, he thinks nothing of it. But soon Alex realizes Brittany is a real person with real problems, and suddenly the bet he made in arrogance turns into something much more. ','2010-04-01','2018-01-04',368,9.94,0,5.07,0,0,'PerfectChemistry.jpg',1,8),
(66,'0446676098','The Notebook','Every so often a love story so captures our hearts that it becomes more than a story-it becomes an experience to remember forever. The Notebook is such a book. It is a celebration of how passion can be ageless and timeless, a tale that moves usto laughter and tears and makes us believe in true love all over again... At thirty-one, Noah Calhoun, back in coastal North Carolina after World War II, is haunted by images of the girl he lost more than a decade earlier. At twenty-nine, socialite Allie Nelson is about to marry a wealthy lawyer, but she cannot stop thinking about the boy who long ago stole her heart. Thus begins the story of a love so enduring and deep it can turn tragedy into triumph, and may even have the power to create a miracle...','1999-12-01','2017-05-19',272,10.50,1.99,5.99,0,0,'TheNotebook.jpg',46,8),
(67,'1476717478','Thoughtless','For almost two years now, Kiera’s boyfriend, Denny, has been everything she’s ever wanted: loving, tender, and endlessly devoted to her. When they head off to a new city to start their lives together—Denny at his dream job and Kiera at a top-notch university—everythingseems perfect. Then an unforeseen obligation forces the happy couple apart. Feeling lonely, confused, and in need of comfort, Kiera turns to an unexpected source—a local rock star named Kellan Kyle. At first, he’s purely a friend she can lean on, but as her loneliness grows, so does their relationship. And then one night everything changes…and none of them will ever be the same.','2012-11-06','2017-12-28',544,18.99,0,11.99,0,0,'Thoughtless.jpg',47,8),
(68,'0770428797','Outlander','In 1945, Claire Randall, a former combat nurse, is back from the war and reunited with her husband on a second honeymoon—when she innocently touches a boulder in one of the ancient stone circles that dot the British Isles. Suddenly she is a Sassenach—an "outlander"—ina Scotland torn by war and raiding border clans in the year of our Lord...1743.Hurled back in time by forces she cannot understand, Claires destiny in soon inextricably intertwined with Clan MacKenzie and the forbidden Castle Leoch. She is catapulted without warning into the intrigues of lairds and spies that may threaten her life ...and shatter her heart. For here, James Fraser, a gallant young Scots warrior, shows her a passion so fierce and a love so absolute that Claire becomes a woman torn between fidelity and desire...and between two vastly different men in two irreconcilable lives.','2001-10-09','2017-07-09',896,12.99,0,9.99,0,0,'Outlander.jpg',48,8),
(69,'0425276767','Bared to You','He was beautiful and brilliant, jagged and white-hot. I was drawn to him as I had never been to anything or anyone in my life. I craved his touch like a drug, even knowing it would weaken me. I was flawed and damaged, and he opened those cracks in me so easily Gideon "knew." He had demons of his own. And we would become the mirrors that reflected each others most private wounds and desires. The bonds of his love transformed me, even as I prayed that the torment of our pasts didnt tear us apart','2012-06-27','2017-09-26',334,18.99,0,10.99,0,0,'BaredtoYou.jpg',49,8),
(70,'1416548947','Gone with the Wind','Many novels have been written about the Civil War and its aftermath. None take us into the burning fields and cities of the American South as Gone With the Wind does, creating haunting scenes and thrilling portraits of characters so vivid that we remember theirwords and feel their fear and hunger for the rest of our lives. In the two main characters, the white-shouldered, irresistible Scarlett and the flashy, contemptuous Rhett, Margaret Mitchell not only conveyed a timeless story of survival under the harshest of circumstances, she also created two of the most famous lovers in theEnglish-speaking world since Romeo and Juliet.','2008-05-20','2017-05-04',1472,12.99,5.99,0.99,0,0,'GonewiththeWind.jpg',50,8),
(71,'1476715904','Slammed','Colleen Hoover’s romantic, emotion-packed debut novel unforgettably captures all the magic and confusion of first love, as two young people forge an unlikely bond before discovering that fate has other plans for them.Following the unexpected death of her father, eighteen-year-old Layken becomes the rock for both her mother and younger brother. She appears resilient and tenacious, but inside, shes losing hope. Then she meets her new neighbor Will, a handsome twenty-one-year-old whose mere presence leaves her flustered and whose passion for poetry slams thrills her.Not long after a heart-stopping first date during which each recognizes something profound and familiar in the other, they are slammed to the core when a shocking discovery brings their new relationship to a sudden halt. Daily interactions become impossibly painful as they struggle to find a balance between the feelings that pull them together and the forces that tear them apart. Only through the poetry they share are they able to speak the truth that is in their hearts and imagine a future where love is cause for celebration, not regret.','2012-09-18','2017-01-22',352,22.00,0,11.99,0,0,'Slammed.jpg',44,8),
(72,'0676976335','The Time Travelers Wife','This is the extraordinary love story of Clare and Henry who met when Clare was six and Henry was thirty-six, and were married when Clare was twenty-two and Henry thirty. Impossible but true, because Henry suffers from a rare condition where his genetic clock periodically resets and he finds himself pulled suddenly into his past or future. In the face of this force they can neither prevent nor control, Henry and Clare’s struggle to lead normal lives is both intensely moving and entirely unforgettable.','2004-06-15','2017-06-11',560,19.99,0,9.99,0,0,'TheTimeTravelersWife.jpg',51,8),
(73,'0749955228','Dark Lover','The only purebred vampire left on the planet and the leader of the Black Dagger Brotherhood, Wrath has a score to settle with the slayers who killed his parents centuries ago. But when his most trusted fighter is killed—orphaning a half-breed daughter unaware of her heritage or her fate—Wrath must put down his dagger and usher the beautiful female into another world.Racked by a restlessness in her body that wasn’t there before, Beth Randall is helpless against the dangerously sexy man who comes to her at night with shadows in his eyes. His tales of the Brotherhood and blood frighten her. Yet his touch ignites a dawning new hunger—one that threatens to consume them both...','2011-02-03','2017-12-27',416,9.99,0,5.99,0,0,'DarkLover.jpg',52,8),
(74,'1416989420','Hush, Hush','For Nora Grey, romance was not part of the plan. At least, not until Patch came along. With his easy smile and probing eyes, Nora is drawn to him against her better judgment. But after a series of terrifying encounters, Noras not sure who to trust—she cant decide whether she should fall into Patchs arms or run and hide from him. And when she tries to seek some answers, she finds herself near a truth more unsettling than any feeling Patch evokes. For Nora stands amid an ancient battle between the immortal and those who have fallen—and choosing the wrong side will cost her life.','2010-09-21','2017-03-11',432,13.99,2.99,4.99,0,0,'Hush,Hush.jpg',1,8),
(75,'0446608955','A Walk to Remember','There was a time when the world was sweeter....when the women in Beaufort, North Carolina, wore dresses, and the men donned hats.... Every April, when the wind smells of both the sea and lilacs, Landon Carter remembers 1958, his last year at Beaufort High.Landon had dated a girl or two, and even once sworn that he had been in love. Certainly the last person he thought he had fall for was Jamie, the shy, almost ethereal daughter of the towns Baptist minister....Jamie, who was destined to show him the depths of the human heart-and the joy and pain of living. The inspiration for this novel came from Nicholas Sparks sister: her life and her courage. From the internationally bestselling author Nicholas Sparks, comes his most moving story yet....','2000-09-01','2017-10-19',256,10.50,0,6.99,0,0,'AWalktoRemember.jpg',46,8),
(76,'143111582','Dune ','Set in the far future amidst a sprawling feudal interstellar empire where planetary dynasties are controlled by noble houses that owe an allegiance to the imperial House Corrino, Dune tells the story of young Paul Atreides (the heir apparent to Duke Leto Atreides and heir of House Atreides) as he and his family accept control of the desert planet Arrakis, the only source of the spice melange, the most important and valuable substance in the cosmos. The story explores the complex, multi-layered interactions of politics, religion, ecology, technology, and human emotion as the forces of the empire confront each other for control of Arrakis.','2006-06-01','2017-10-19',604,13.49,0,5.20,0,0,'dune.jpg',53,8),
(77,'765394863','Enders Game','In order to develop a secure defense against a hostile alien races next attack, government agencies breed child geniuses and train them as soldiers. A brilliant young boy, Andrew "Ender" Wiggin lives with his kind but distant parents, his sadistic brother Peter, and the person he loves more than anyone else, his sister Valentine. Peter and Valentine were candidates for the soldier-training program but did not make the cut—young Ender is the Wiggin drafted to the orbiting Battle School for rigorous military training.','2010-4-1','2018-1-20',264,8.45,0,3.50,0,0,'enders_game.jpg',56,8),
(78,'618968636','The Hobbit','J.R.R. Tolkiens own description for the original edition: "If you care for journeys there and back, out of the comfortable Western world, over the edge of the Wild, and home again, and can take an interest in a humble hero (blessed with a little wisdom and a little courage and considerable good luck), here is a record of such a journey and such a traveler. The period is the ancient time between the age of Faerie and the dominion of men, when the famous forest of Mirkwood was still standing, and the mountains were full of danger. In following the path of this humble adventurer, you will learn by the way (as he did) -- if you do not already know all about these things -- much about trolls, goblins, dwarves, and elves, and get some glimpses into the history and politics of a neglected but important period. For Mr. Bilbo Baggins visited various notable persons; conversed with the dragon, Smaug the Magnificent; and was present, rather unwillingly, at the Battle of the Five Armies. This is all the more remarkable, since he was a hobbit. Hobbits have hitherto been passed over in history and legend, perhaps because they as a rule preferred comfort to excitement. But this account, based on his personal memoirs, of the one exciting year in the otherwise quiet life of Mr. Baggins will give you a fair idea of the estimable people now (it is said) becoming rather rare. They do not like noise."','1931-09-21','2018-1-19',320,11.02,0,4.50,0,0,'hobbit.jpg',55,8),
(79,'606396594','American Gods','Locked behind bars for three years, Shadow did his time, quietly waiting for the magic day when he could return to Eagle Point, Indiana. A man no longer scared of what tomorrow might bring, all he wanted was to be with Laura, the wife he deeply loved, and start a new life.','2001-06-19','2001-1-22',674,11.99,0,5.00,0,0,'american_gods.jpg',42,8),
(80,'312536631','The forever war','Conscripted into service for the United Nations Exploratory Force, a highly trained unit built for revenge, physics student William Mandella fights for his planet light years away against the alien force known as the Taurans. “Mandella’s attempt to survive and remain human in the face of an absurd, almost endless war is harrowing, hilarious, heartbreaking, and true,” says Pulitzer Prize–winning novelist Junot Díaz—and because of the relative passage of time when one travels at incredibly high speed, the Earth Mandella returns to after his two-year experience has progressed decades and is foreign to him in disturbing ways.','1974-10-31','2018-01-19',274,10.44,0,5.00,0,0,'forever_war.jpg',56,8),
(81,'307346617','world war z','We survived the zombie apocalypse, but how many of us are still haunted by that terrible time? We have (temporarily?) defeated the living dead, but at what cost? Told in the haunting and riveting voices of the men and women who witnessed the horror firsthand, World War Z, a #1 New York Times bestseller and the basis for the blockbuster movie, is the only record of the plague years.','2016-09-16','2018-01-19',352,10.19,0,4.50,0,0,'world_war_z.jpg',3,8),
(82,'006051275','The Dispossessed','centuries ago, the moon Anarres was settled by utopian anarchists who left the Earthlike planet Urras in search of a better world, a new beginning. Now a brilliant physicist, Shevek, determines to reunite the two civilizations that have been separated by hatred since long before he was born.','2000-09-01','1974-05-1',400,9.99,0,5.00,0,0,'the_dispossessed.jpg',28,8),
(83,'1524796972','Do Androids Dream of Electric Sheep?','World War Terminus had left the Earth devastated. Through its ruins, bounty hunter Rick Deckard stalked, in search of the renegade replicants who were his prey. When he was not "retiring" them with his laser weapon, he dreamed of owning a live animal - the ultimate status symbol in a world all but bereft of animal life.','1968-05-18','2018-01-19',240,6.75,0,2.99,0,0,'androids.jpg',57,8),
(84,'765379945','All the Birds in the Sky','An ancient society of witches and a hipster technological startup go to war in order to prevent the world from tearing itself apart. To further complicate things, each of the groups’ most promising followers (Patricia, a brilliant witch and Laurence, an engineering “wunderkind”) may just be in love with each other.','2016-01-26','2018-10-20',317,10.99,0,3.50,0,0,'all_the_birds.jpg',54,8),
(85,'808598295','The Handmaids Tale ','In this multi-award-winning, bestselling novel, Margaret Atwood has created a stunning Orwellian vision of the near future. This is the story of Offred, one of the unfortunate “Handmaids” under the new social order who have only one purpose: to breed. In Gilead, where women are prohibited from holding jobs, reading, and forming friendships, Offred’s persistent memories of life in the “time before” and her will to survive are acts of rebellion. Provocative, startling, prophetic, and with Margaret Atwood’s devastating irony, wit, and acute perceptive powers in full force, The Handmaid’s Tale is at once a mordant satire and a dire warning.','1985-12-10','2018-10-19',370,11.12,0,5.50,0,0,'handmaid.jpg',58,8),
(86,'804139024','The Martian','After a dust storm nearly kills him and forces his crew to evacuate while thinking him dead, Mark finds himself stranded and completely alone with no way to even signal Earth that he’s alive—and even if he could get word out, his supplies would be gone long before a rescue could arrive. ','2011-02-11','2018-10-19',385,11.50,0,3.99,0,0,'martian.jpg',3,8),
(87,'307887448','Ready Player One','At once wildly original and stuffed with irresistible nostalgia, READY PLAYER ONE is a spectacularly genre-busting, ambitious, and charming debut—part quest novel, part love story, and part virtual space opera set in a universe where spell-slinging mages battle giant Japanese robots, entire planets are inspired by Blade Runner, and flying DeLoreans achieve light speed.','2011-08-16','2018-10-19',386,9.27,0,4.0,0,0,'player_one.jpg',3,8),
(88,'808514571','slaughterhouse five','Selected by the Modern Library as one of the 100 best novels of all time, Slaughterhouse-Five, an American classic, is one of the worlds great antiwar books. Centering on the infamous firebombing of Dresden, Billy Pilgrims odyssey through time reflects the mythic journey of our own fractured lives as we search for meaning in what we fear most.','1969-03-01','2018-10-19',226,8.99,0,2.99,0,0,'slaughterhouse.jpg',59,8),
(89,'1500453307','The Long Way to a Small, Angry Planet','When Rosemary Harper joins the crew of the Wayfarer, she is not expecting much. The Wayfarer, a patched-up ship that is seen better days, offers her everything she could possibly want: a small, quiet spot to call home for a while, adventure in far-off corners of the galaxy, and distance from her troubled past.','2000-09-01','2015-03-16',519,3.50,0,0.99,0,0,'long_way.jpg',53,8),
(90,'553448129','Artemis','Jasmine Bashara never signed up to be a hero. She just wanted to get rich. Not crazy, eccentric-billionaire rich, like many of the visitors to her hometown of Artemis, humanity’s first and only lunar colony. Just rich enough to move out of her coffin-sized apartment and eat something better than flavored algae. Rich enough to pay off a debt she’s owed for a long time.','2017-11-12','2018-1-19',322,15.50,0,3.99,0,0,'artemis.jpg',61,8),
(91,'110196703X','Childhoods End','Childhood’s End is one of the defining legacies of Arthur C. Clarke, the author of 2001: A Space Odyssey and many other groundbreaking works. Since its publication in 1953, this prescient novel about first contact gone wrong has come to be regarded not only as a science fiction classic but as a literary thriller of the highest order.','1953-08-24','2018-1-19',226,7.50,0,1.50,0,0,'childhood.jpg',62,8),
(92,'156027607','Solaris','Despite two films made with panache „Solaris” remains a book constantly rediscovered by new generations of readers. The moving story of contact with alien intelligence serves as a canvas for discussion of our mind’s limitations and the nature of human cognition. A love story for some readers, a philosophical treatise for others; Lem’s inspiring masterpiece defies unambiguous interpretations.','2002-08-02','2017-10-19',224,7.50,0,2.99,0,0,'solaris.jpg',63,8),
(93,'1781084491','Ninefox Gambit','When Captain Kel Cheris of the hexarchate is disgraced for her unconventional tactics, Kel Command gives her a chance to redeem herself, by retaking the Fortress of Scattered Needles from the heretics. Cheris’s career isn’t the only thing at stake: if the fortress falls, the hexarchate itself might be next.','2014-06-04','2018-1-19',384,15.50,0,4.99,0,0,'gambit.jpg',64,8),
(94,'765377101','Deaths End','Three-Body was released to great acclaim including coverage in The New York Times and The Wall Street Journal. It was also named a finalist for the Nebula Award, making it the first translated novel to be nominated for a major SF award since Italo Calvinos Invisible Cities in 1976.','2016-09-26','2017-10-19',608,23.50,0,8.50,0,0,'death.jpg',64,8),
(95,'765377063','The Three-Body Problem','Set against the backdrop of Chinas Cultural Revolution, a secret military project sends signals into space to establish contact with aliens. An alien civilization on the brink of destruction captures the signal and plans to invade Earth. Meanwhile, on Earth, different camps start forming, planning to either welcome the superior beings and help them take over a world seen as corrupt, or to fight against the invasion. The result is a science fiction masterpiece of enormous scope and vision.','2014-11-11','2017-10-19',400,18.50,0,9.99,0,0,'problem.jpg',64,8),
(96,'076537708X','The Dark Forest','In The Dark Forest, Earth is reeling from the revelation of a coming alien invasion-in just four centuries time. The aliens human collaborators may have been defeated, but the presence of the sophons, the subatomic particles that allow Trisolaris instant access to all human information, means that Earths defense plans are totally exposed to the enemy. Only the human mind remains a secret. This is the motivation for the Wallfacer Project, a daring plan that grants four men enormous resources to design secret strategies, hidden through deceit and misdirection from Earth and Trisolaris alike. Three of the Wallfacers are influential statesmen and scientists, but the fourth is a total unknown. Luo Ji, an unambitious Chinese astronomer and sociologist, is baffled by his new status. All he knows is that he is the one Wallfacer that Trisolaris wants dead.','2015-08-11','2017-10-19',513,22.50,0,8.50,0,0,'forest.jpg',54,8),
(97,'374386137','A Wrinkle in Time','It was a dark and stormy night; Meg Murry, her small brother Charles Wallace, and her mother had come down to the kitchen for a midnight snack when they were upset by the arrival of a most disturbing stranger.','1962-01-01','2017-10-19',216,8.50,0,2.99,0,0,'wrinckle.jpg',22,8),
(98,'031626234X','New York 2140','As the sea levels rose, every street became a canal. Every skyscraper an island. For the residents of one apartment building in Madison Square, however, New York in the year 2140 is far from a drowned city.','2017-03-14','2017-10-19',664,10.50,0,6.99,0,0,'2140.jpg',65,8),
(99,'055338256X','I, Robot','Here are stories of robots gone mad, of mind-read robots, and robots with a sense of humor. Of robot politicians, and robots who secretly run the world--all told with the dramatic blend of science fact and science fiction that has become Asmiovs trademark.','2009-04-19','2017-10-19',256,10.50,0,6.99,0,0,'i_robot.jpg',66,8),
(100,'848805224','Stranger in a Strange Land','Valentine Michael Smith is a man raised by Martians. Sent to Earth, he must learn what it is to be human. But his beliefs and his powers far exceed the limits of man, and his arrival leads to a transformation that will alter Earth’s inhabitants forever...','1991-08-11','2017-10-19',530,13.50,0,4.99,0,0,'stranger.jpg',67,8),
(101, '0736420959', 'The Lion King', 'A young lion prince is born in Africa, thus making his uncle Scar the second in line to the throne. Scar plots with the hyenas to kill King Mufasa and Prince Simba, thus making himself King. The King is killed and Simba is led to believe by Scar that it was his fault, and so flees the kingdom in shame.','2003-09-09','2018-11-26', 24,999.99,999.98,1099.99,0,0, 'lionking.jpg',68,7);

INSERT INTO Book_Genre(book_id, genre_id) VALUES
(1,1),
(2,1),
(3,1),
(4,1),
(5,1),
(6,1),
(7,1),
(8,1),
(9,1),
(10,1),
(11,1),
(12,1),
(13,1),
(14,1),
(15,1),
(16,1),
(17,1),
(18,1),
(19,1),
(20,1),
(21,1),
(22,1),
(23,1),
(24,1),
(25,1),
(26,2),
(27,2),
(28,2),
(29,2),
(30,2),
(31,2),
(32,2),
(33,2),
(34,2),
(35,2),
(36,2),
(37,2),
(38,2),
(39,2),
(40,2),
(41,2),
(42,2),
(43,2),
(44,2),
(45,2),
(46,2),
(47,2),
(48,2),
(49,2),
(50,2),
(51,3),
(52,3),
(52,4),
(53,3),
(54,3),
(55,3),
(55,5),
(56,3),
(56,6),
(57,3),
(57,6),
(58,3),
(59,3),
(59,7),
(60,3),
(60,6),
(60,8),
(61,3),
(61,7),
(62,3),
(62,4),
(62,8),
(63,4),
(64,4),
(64,8),
(65,4),
(65,8),
(66,4),
(66,8),
(67,4),
(68,4),
(68,6),
(68,9),
(69,4),
(70,4),
(70,8),
(70,9),
(71,4),
(71,8),
(72,4),
(72,6),
(72,8),
(73,4),
(73,8),
(74,4),
(74,6),
(74,10),
(75,4),
(75,11),
(76,5),
(76,6),
(77,5),
(78,5),
(78,6),
(79,6),
(80,5),
(81,5),
(82,5),
(83,5),
(84,5),
(85,13),
(85,14),
(85,5),
(86,5),
(87,5),
(88,5),
(88,16),
(88,15),
(89,5),
(90,5),
(91,5),
(92,5),
(93,5),
(94,5),
(95,5),
(96,5),
(97,5),
(98,5),
(99,5),
(100,5),
(101,12),
(101,4),
(101,6),
(101,11);

INSERT INTO Book_Author(book_id, author_id) VALUES
(1,1),
(2,2),
(3,3),
(4,4),
(5,5),
(6,6),
(7,7),
(8,8),
(9,9),
(10,10),
(11,11),
(12,12),
(13,13),
(14,14),
(15,15),
(16,16),
(17,17),
(18,18),
(19,19),
(20,20),
(21,21),
(22,9),
(23,20),
(24,6),
(25,1),
(26,23),
(27,24),
(28,25),
(29,26),
(30,27),
(31,28),
(32,29),
(33,30),
(34,31),
(35,32),
(36,33),
(37,27),
(38,34),
(39,31),
(40,35),
(41,36),
(41,37),
(41,38),
(42,39),
(43,40),
(44,41),
(45,42),
(46,30),
(47,43),
(48,44),
(49,45),
(50,27),
(51,46),
(52,47),
(53,48),
(54,49),
(55,50),
(56,51),
(57,51),
(58,52),
(59,53),
(60,54),
(61,47),
(62,55),
(63,56),
(64,57),
(65,58),
(66,59),
(67,60),
(68,61),
(69,62),
(70,63),
(70,64),
(71,65),
(72,66),
(73,67),
(74,68),
(75,59),
(76,69),
(77,70),
(78,71),
(79,72),
(80,73),
(81,74),
(82,75),
(83,76),
(84,77),
(85,78),
(86,79),
(87,80),
(88,81),
(89,82),
(90,79),
(91,83),
(91,84),
(92,85),
(93,86),
(94,86),
(95,86),
(96,86),
(97,86),
(98,87),
(99,89),
(100, 90),
(101, 91);

INSERT INTO Country(country) 
    VALUES('Canada');

INSERT INTO Province (province) VALUES
('Quebec'),
('Ontario'),
('Manitoba'),
('Saskatchewan'),
('Alberta'),
('Bitish Columbia'),
('Nova Scotia'),
('Newfoundland and Labrador'),
('New Brunswick'),
('Prince Edward Island'),
('Nunavut'),
('Northwest Territories'),
('Yukon');

INSERT INTO Ad (title, image, ad_link, chosen) VALUES
('Honda', 'honda.jpg', 'https://www.honda.ca/', 1),
('Asus', 'asus.jpg', 'https://www.asus.com/ca-en/', 1),
('BMW', 'bmw.jpg', 'https://www.bmw.ca/en/home.html', 0),
('Intel', 'intelcorei9.jpg', 'https://www.intel.ca/content/www/ca/en/homepage.html', 0),
('ESL', 'esl.jpg', 'https://www.intel.ca/content/www/ca/en/homepage.html', 0),
('Fornite', 'fortnite.jpg', 'https://www.epicgames.com/fortnite/en-US/buy-now/battle-royale', 0);

INSERT INTO Rss_News(title, link, display) VALUES
('CTV Top Stories', 'http://www.cbc.ca/cmlink/rss-topstories', 1);

INSERT INTO Survey(question, chosen)
VALUES('What book genre is your favorite?', true);

INSERT INTO Response(total_answer, answer) VALUES
(1, 'Horror'),
(2, 'Science Fiction'),
(3, 'Romance'),
(4, 'Drama');

INSERT INTO Survey_Response(survey_id, response_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4);

INSERT INTO Tax(province_id, gst, pst, hst)
VALUES(1, 14.975, 0, 0);


/* Test data */

INSERT INTO Client(password, title, username, email, last_name, first_name, company, address_one, address_two, postal_code, home_tel, cell_tel, province_id, city, country_id, manager, first_login) VALUES
('pw', 'Mr.', 'aleciot', 'alessandromciotola@gmail.com', 'Ciotola', 'Alessandro', 'none', '123 address', null, 'H1E5C8', '5142342345', '5141231234', 1, 'Terrebonne', 1, 0, 0),
('pw', 'Mr.', 'kaybee', 'kevbui89@gmail.com', 'bui', 'kevin', 'none', '123 address', null, 'H1E5C8', '5143456756', '5148955133', 1, 'Montreal', 1, 1, 0),
('pw', 'Mr.', 'kaybee1', 'kb@gmail.com', 'bui', 'kevin', 'none', '123 address', null, 'H1E5C8', '5143456756', '5148955133', 1, 'Montreal', 1, 0, 1),
('pw', 'Mr.', 'el_tigre', 'eltigre@gmail.com', 'tigre', 'el', 'none', '123 address', null, 'H1E5C8', '5142342345', '5141231234', 1, 'Montreal', 1, 1, 1),
('dawsoncollege', 'Mr.', 'DawsonConsumer', 'cst.send@gmail.com', 'Consumer', 'Dawson', 'none', '123 Dawson Consumer', null, 'H1E5C8', '5142342345', '5141231234', 1, 'Montreal', 1, 0, 1),
('collegedawson', 'Mr.', 'DawsonManager', 'cst.receive@gmail.com', 'Manager', 'Dawson', 'none', '123 Dawson Manager', null, 'H1E5C8', '5142342345', '5141231234', 1, 'Montreal', 1, 1, 1);

INSERT INTO Master_Invoice(client_id, sale_date, net_value, gross_value) VALUES
(1, '2018-02-11', 11.98, 13.77),
(1, '2018-03-01', 999.99, 1149.74),
(2, '2018-03-02', 999.99, 1149.74),
(2, '2018-01-22', 11.98, 13.77);

INSERT INTO Detail_Invoice(invoice_id, book_id, book_price, gst, pst, hst, status) VALUES
(1, 100, 4.99, 14.975, 0, 0, 1),
(1, 99, 6.99, 14.975, 0, 0, 1),
(2, 101, 999.99, 14.975, 0, 0, 1),
(3, 101, 999.99, 14.975, 0, 0, 1),
(4, 100, 4.99, 14.975, 0, 0, 1),
(4, 99, 6.99, 14.975, 0, 0, 1);

INSERT INTO Review (client_id, book_id, review_date, comment_title, comment, rating, valid, pending) VALUES
(1, 101, '2018-02-11', 'Good', 'this book is good', 4, 0, 1),
(1, 101, '2018-02-11', 'cute', 'this book is ok', 3, 0, 1),
(1, 100, '2018-02-11', 'Good', 'this book is ok', 3, 0, 1);

UPDATE Book SET total_sale = 2 WHERE book_id = 101; 
UPDATE Book SET total_sale = 2 WHERE book_id = 100; 
UPDATE Book SET total_sale = 2 WHERE book_id = 99; 