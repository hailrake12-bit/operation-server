package web.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DataBaseInitializer {
    private final static String local = "src/main/resources/";

    private final static Path questionsPath = Paths.get( local + "questions.txt");
    private final static Path booksPath = Paths.get( local + "books.txt");

    public static void Initialize(){
        try(
                Connection connection = DriverManager.getConnection(DataBase.getURL(), DataBase.getUser(), DataBase.getPassword());
                Statement statement = connection.createStatement();
                BufferedReader questionReader = Files.newBufferedReader(questionsPath);
            BufferedReader bookReader = Files.newBufferedReader(booksPath)
        ){
            createTables(connection);
            insertQuestions(connection, questionReader);
            insertBooks(connection, bookReader);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void createTables(Connection connection){
        String tablesQuery = """
                CREATE TABLE IF NOT EXISTS public.users (
                    username varchar NOT NULL,
                    "password" varchar NOT NULL,
                    PRIMARY KEY (username)
                );
                CREATE TABLE IF NOT EXISTS public.questions (
                    question_id int GENERATED ALWAYS AS IDENTITY NOT NULL,
                    question varchar NOT NULL,
                    answer1 varchar NOT NULL,
                    answer2 varchar NOT NULL,
                    answer3 varchar NOT NULL,
                    answer4 varchar NOT NULL,
                    theme varchar NOT NULL,
                    correct_answer varchar NOT NULL,
                    CONSTRAINT question_pk PRIMARY KEY (question_id),
                    CONSTRAINT unique_question UNIQUE (question)
                );
                CREATE TABLE IF NOT EXISTS public.users_grades (
                    username varchar NOT NULL,
                    theme varchar NOT NULL,
                    grade real,
                    CONSTRAINT users_grades_pk PRIMARY KEY (username, theme),
                    FOREIGN KEY (username) REFERENCES public.users(username)
                    ON DELETE CASCADE
                );
                CREATE TABLE IF NOT EXISTS public.users_books (
                    id int GENERATED ALWAYS AS IDENTITY NOT NULL,
                    username varchar NOT NULL,
                    theme varchar NOT NULL,
                    book_name varchar NOT NULL,
                    is_read boolean default false,
                    CONSTRAINT users_books_pk PRIMARY KEY (id),
                    FOREIGN KEY (username) REFERENCES public.users(username)
                    ON DELETE CASCADE
                );
                CREATE TABLE IF NOT EXISTS  public.books (
                    theme varchar NULL,
                    "name" varchar NULL,
                    "text" text NULL,
                    CONSTRAINT books_pk PRIMARY KEY (theme,name)
                );
            """;
        try(Statement statement = connection.createStatement()){
            statement.executeUpdate(tablesQuery);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    private static void insertQuestions(Connection connection, BufferedReader questionReader) throws IOException{
        String line;
        while ((line = questionReader.readLine()) != null) {
            // Разделяем строку на составляющие
            String[] columns = line.split("#");

            if (columns.length == 7) {  // Проверяем, что в строке 7 элементов
                String question = columns[0];
                String answer1 = columns[1];
                String answer2 = columns[2];
                String answer3 = columns[3];
                String answer4 = columns[4];
                String theme = columns[5];
                String correctAnswer = columns[6];

                String sql = "INSERT INTO questions (question, answer1, answer2, answer3, answer4, theme, correct_answer) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) ON CONFLICT (question) DO NOTHING";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, question);
                    ps.setString(2, answer1);
                    ps.setString(3, answer2);
                    ps.setString(4, answer3);
                    ps.setString(5, answer4);
                    ps.setString(6, theme);
                    ps.setString(7, correctAnswer);

                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Некорректная строка: " + line);
            }
        }
    }

    private static void insertBooks(Connection connection, BufferedReader bookReader) throws IOException {
        String name = null;
        String theme = null;
        StringBuilder text = new StringBuilder();

        String sql = "INSERT INTO books (theme, name, text) VALUES (?, ?, ?) ON CONFLICT (theme, name) DO NOTHING";

        String line;
        while ((line = bookReader.readLine()) != null) {
            if (line.startsWith("book")) {
                String[] parts = line.split("#");
                name = parts[0];
                theme = parts[1];
            } else if (line.endsWith("#$#")) {
                text.append(line, 0, line.length()-3);
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, theme);
                    stmt.setString(2, name);
                    stmt.setString(3, text.toString());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                name = null;
                theme = null;
                text = new StringBuilder();
            }
        }
    }
}
