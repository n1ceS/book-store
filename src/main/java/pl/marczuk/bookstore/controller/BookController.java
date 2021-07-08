package pl.marczuk.bookstore.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.marczuk.bookstore.dto.BookDto;
import pl.marczuk.bookstore.model.Author;
import pl.marczuk.bookstore.model.Book;
import pl.marczuk.bookstore.model.Category;
import pl.marczuk.bookstore.model.Publisher;
import pl.marczuk.bookstore.service.*;
import pl.marczuk.bookstore.utils.FileUploadUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final PublisherService publisherService;
    private final BookService bookService;
    private final ShoppingCartService shoppingCartService;

    public BookController(AuthorService authorService, CategoryService categoryService, PublisherService publisherService, BookService bookService, ShoppingCartService shoppingCartService) {
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.publisherService = publisherService;
        this.bookService = bookService;
        if(bookService == null) System.out.println("PX");
        this.shoppingCartService = shoppingCartService;
    }


    @GetMapping
    public String books(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("itemsCount", shoppingCartService.getItemsCount());
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addBook(Model model) {
        BookDto bookDto = new BookDto();
        List<Author> authors = authorService.getAllAuthors();
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("book", bookDto);
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categories);
        return "admin/addBook";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addBook( @Valid @ModelAttribute("book") BookDto bookDto,BindingResult bindingResult, @RequestParam("image") MultipartFile multipartFile, Model model) throws IOException {
        if(bindingResult.hasErrors())
        {
            List<Author> authors = authorService.getAllAuthors();
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("book", bookDto);
            model.addAttribute("authors", authors);
            model.addAttribute("categories", categories);
            return "admin/addBook";
        }
        Book book = convertToEntity(bookDto);
        if(multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            book.setPhoto(fileName);
            String uploadDir = "src/main/resources/static/img/book-images/";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        bookService.add(book);

        return "redirect:/books";
    }

    @GetMapping("/edit/{isbn}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editBook(@PathVariable(name = "isbn") String isbn, Model model) {
        Book book = bookService.findByIsbn(isbn);
        List<Author> authors = authorService.getAllAuthors();
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categories);
        return "admin/editBook";
    }


    @PostMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editBook(@Valid @ModelAttribute("book") BookDto bookDto, BindingResult bindingResult, @RequestParam("image")  MultipartFile multipartFile, Model model) throws IOException {
        if(bindingResult.hasErrors())
        {
            List<Author> authors = authorService.getAllAuthors();
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("book", bookDto);
            model.addAttribute("authors", authors);
            model.addAttribute("categories", categories);
            return "/admin/editBook";
        }
        Book book = convertToEntity(bookDto);
        if(multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            book.setPhoto(fileName);
            String uploadDir = "src/main/resources/static/img/book-images/";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        bookService.editBook(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{isbn}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteBook(@PathVariable(name = "isbn") String isbn) {
        bookService.delete(isbn);
        return "redirect:/books";
    }

    private Book convertToEntity(BookDto bookDto) {
        Book book = new Book();
        book.setQuantity(bookDto.getQuantity());
        Publisher publisher = new Publisher(bookDto.getPublisher());
        publisher = publisherService.addPublisher(publisher);
        book.setPublisher(publisher);
        book.setPublication_year(bookDto.getPublication_year());
        book.setAuthor(bookDto.getAuthor());
        book.setTitle(bookDto.getTitle());
        book.setPrice(bookDto.getPrice());
        book.setCategory(bookDto.getCategory());
        book.setIsbn(bookDto.getIsbn());
        book.setPhoto(bookDto.getPhoto());
        return book;
    }
}
