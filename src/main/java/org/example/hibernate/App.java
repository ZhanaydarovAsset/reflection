package org.example.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Random;

public class App {
    public static void main(String[] args) {
        Configuration configuration = new Configuration().configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {

            insertData(sessionFactory);

            List<Student> studentsOlder = findStudentsOlder(sessionFactory);
            studentsOlder.forEach(System.out::println);

            try(Session session = sessionFactory.openSession()) {
                Student studentFind = session.find(Student.class, 20l);
                System.out.println(studentFind);
            }
        }

    }

    private static void insertData(SessionFactory sessionFactory) {
        Random random = new Random();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            for (int i = 1; i <= 20; i++) {
                Student student = new Student();
                student.setId((long) i);
                student.setFirstName("Srudent #" + i);
                student.setSecondeName("LastName #" + i);
                student.setAge(18L + random.nextInt(6));
                session.persist(student);
            }
            transaction.commit();
        }

    }

    public static List<Student> findStudentsOlder(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Student WHERE age > 20").list();
        }
    }

}
