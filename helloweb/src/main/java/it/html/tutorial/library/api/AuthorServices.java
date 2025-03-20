package it.html.tutorial.library.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("books/{book_id}/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorServices{
	@GET
	public List list(@PathParam("{book_id}") long bookId) {
		Author author = new Author();
		author.setId(1);
		author.setName("Joanne");
		author.setSurname("Rowling");
		List authors = new ArrayList();
		authors.add(author);
		return authors;
	}
}
