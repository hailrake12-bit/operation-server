package web.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class DataBaseInitializer {
    private static DataBase db = new DataBase();
    private static String filePath = "src/main/resources/questions.txt";

    public static void Initialize(){
        try(
            Connection connection = DriverManager.getConnection(db.getURL(), db.getUser(), db.getPassword());
            Statement statement = connection.createStatement();
            BufferedReader reader = new BufferedReader(new FileReader(filePath))
        ){
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS public.users (
                    user_id int GENERATED ALWAYS AS IDENTITY NOT NULL,
                    username varchar NOT NULL,
                    "password" varchar NOT NULL,
                    PRIMARY KEY (user_id)
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
                	grade_id int GENERATED ALWAYS AS IDENTITY NOT NULL,
                	user_id int NOT NULL,
                	theme varchar NOT NULL,
                	grade int NOT NULL,
                	CONSTRAINT users_grades_pk PRIMARY KEY (grade_id),
                    FOREIGN KEY (user_id) REFERENCES public.users(user_id)
                    ON DELETE CASCADE
                );
                
            """);


            String line;
            while ((line = reader.readLine()) != null) {
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

                    // Вставляем данные в базу
                    insertQuestion(connection, question, answer1, answer2, answer3, answer4, theme, correctAnswer);
                } else {
                    System.out.println("Некорректная строка: " + line);
                }
            }


        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void insertQuestion(Connection connection, String question, String answer1, String answer2, String answer3, String answer4, String theme, String correctAnswer) {
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
    }
}
