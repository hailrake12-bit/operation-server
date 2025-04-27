package web.responses.entities;

import web.responses.Body;

import java.util.ArrayList;

public class Books implements Body {
    ArrayList<Book> books = new ArrayList<Book>();

    public void addBook(Book book){
        books.add(book);
    }

    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder();
        if(books.size()>1) json.append("[\n");

        for (int i = 0; i < books.size(); i++) {
            json.append(books.get(i).toJson());
            if (i < books.size() - 1) {
                json.append(",\n"); // запятая между объектами
            }
        }

        if(books.size()>1) json.append("\n]");
        return json.toString();
    }

}
