package it.html.tutorial.library.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.ws.rs.*;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Path("books")
public class BookServices {

    private static final String FILE_PATH = "libri.json";
    private Gson gson = new Gson();

    private List<Book> read() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            FileReader reader = new FileReader(file);
            Type listType = new TypeToken<List<Book>>() {}.getType();
            List<Book> books = gson.fromJson(reader, listType);
            reader.close();
            return books;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void write(List<Book> books) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(books, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> list() {
        return read();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") long id) {
        List<Book> books = read();
        for (Book b : books) {
            if (b.getId() == id) {
                return Response.ok(b).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Book book) throws URISyntaxException {
        List<Book> books = read();
        long newId = books.size() + 1;
        book.setId(newId);
        books.add(book);
        write(books);
        return Response.created(new URI("api/books/" + newId)).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, Book bn) {
        List<Book> books = read();
        for (Book b : books) {
            if (b.getId() == id) {
                b.setTitle(bn.getTitle());
                b.setLanguage(bn.getLanguage());
                b.setAuthors(bn.getAuthors());
                write(books);
                return Response.noContent().build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") long id) {
        List<Book> books = read();
        Book bookToDelete = null;
        for (Book b : books) {
            if (b.getId() == id) {
            	books.remove(bookToDelete);
                write(books);
                return Response.noContent().build();
            }
        }
        
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}