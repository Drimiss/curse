create table roles (
                       id int(15) not null auto_increment,
                       name varchar(50) not null unique,
                       primary key (id));

create table users (
                       id int(50) not null auto_increment,
                       username varchar(50) not null unique,
                       password varchar(255) not null,
                       role_id int(15) not null,
                       full_name varchar(100) not null,
                       email varchar(100) unique,
                       phone varchar(15),
                       primary key (id),
                       foreign key (role_id) references roles(id)
)engine = InnoDB default charset = UTF8;

create table courses (
                         id int(15) not null auto_increment,
                         course_name varchar(100) not null,
                         description text,
                         teacher_id int,
                         primary key (id),
                         foreign key (teacher_id) references users(id) on delete set null
)engine = InnoDB default charset = UTF8;



create table materials (
                           id int(15) not null auto_increment,
                           course_id int(15),
                           title varchar(100) not null,
                           file_path varchar(255) not null,
                           primary key (id),
                           foreign key (course_id) references courses(id) on delete cascade
)engine = InnoDB default charset = UTF8;

create table schedule (
                          id int(15) not null auto_increment,
                          course_id int(15),
                          date date not null,
                          start_time time not null,
                          end_time time not null,
                          room varchar(50),
                          primary key (id),
                          foreign key (course_id) references courses(id) on delete cascade
)engine = InnoDB default charset = UTF8;

create table grades (
                        id int(15) not null auto_increment,
                        student_id int(15) not null,
                        course_id int(15) not null,
                        grade decimal(4, 2) check (grade >= 0 AND grade <= 100),
                        comment text,
                        date timestamp,
                        primary key (id),
                        foreign key (student_id) references users(id) on delete cascade,
                        foreign key (course_id) references courses(id) on delete cascade
)engine = InnoDB default charset = UTF8;

create table attendance (
                            id int(15) not null auto_increment,
                            student_id int(15),
                            schedule_id int(15),
                            status varchar(20) check (status in ('Присутствовал', 'Отсутствовал')) not null,
                            date date not null,
                            primary key (id),
                            foreign key (student_id) references users(id) on delete cascade,
                            foreign key (schedule_id) references schedule(id) on delete cascade
)engine = InnoDB default charset = UTF8;




# drop table users;
# drop table roles;
# drop table courses;
# drop table materials;
# drop table schedule;
# drop table grades;
# drop table attendance;
# drop table attendance;