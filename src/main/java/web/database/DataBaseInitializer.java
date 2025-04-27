package web.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DataBaseInitializer {
    private static String local = "src/main/resources/";

    private static Path questionsPath = Paths.get("questions.txt");
    private static Path booksPath = Paths.get( "books.txt");

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
                    questionid int GENERATED ALWAYS AS IDENTITY NOT NULL,
                    question varchar NOT NULL,
                    answer1 varchar NOT NULL,
                    answer2 varchar NOT NULL,
                    answer3 varchar NOT NULL,
                    answer4 varchar NOT NULL,
                    theme varchar NOT NULL,
                    correct_answer varchar NOT NULL,
                    CONSTRAINT questionn_pk PRIMARY KEY (questionid),
                    CONSTRAINT unique_question UNIQUE (question)
                );
                CREATE TABLE IF NOT EXISTS public.users_grades (
                    username varchar NOT NULL,
                    theme varchar NOT NULL,
                    grade real,
                    book1 BOOLEAN DEFAULT FALSE,
                    book2 BOOLEAN DEFAULT FALSE,
                    book3 BOOLEAN DEFAULT FALSE,
                    book4 BOOLEAN DEFAULT FALSE,
                    book5 BOOLEAN DEFAULT FALSE,
                    CONSTRAINT users_grades_pk PRIMARY KEY (username, theme),
                    FOREIGN KEY (username) REFERENCES public.users(username)
                    ON DELETE CASCADE
                );
                CREATE TABLE IF NOT EXISTS  public.books (
                    theme varchar NULL,
                    book varchar NULL,
                    "text" text NULL,
                    CONSTRAINT books_pk PRIMARY KEY (theme,book)
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
        String book = null;
        String theme = null;
        StringBuilder text = new StringBuilder();

        String sql = "INSERT INTO public.books (theme, book, text) VALUES (?, ?, ?) ON CONFLICT (theme, book) DO NOTHING";

        String line;
        while ((line = bookReader.readLine()) != null) {
            if (line.startsWith("book")) {
                String[] parts = line.split("#");
                book = parts[0];
                theme = parts[1];
            } else if (line.equals("#$#")) {
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, theme);
                    stmt.setString(2, book);
                    stmt.setString(3, text.toString());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                book = null;
                theme = null;
                text = new StringBuilder();
            } else {
                text.append(line).append("\n");
            }
        }
    }
}
