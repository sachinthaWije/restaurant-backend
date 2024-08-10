package sachi.dev.restaurant.response;

import lombok.Data;
import org.springframework.data.domain.Page;
import sachi.dev.restaurant.model.Menu;

import java.util.List;

@Data
public class SearchResponse {
    private List<Menu> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    public SearchResponse(Page<Menu> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }

}
