package sachi.dev.restaurant.response;

import lombok.Data;
import org.springframework.data.domain.Page;
import sachi.dev.restaurant.dto.MenuDTO;
import sachi.dev.restaurant.model.Menu;

import java.util.List;

@Data
public class SearchResponse {
    private List<Menu> content;
    private List<MenuDTO> contentDto;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    public SearchResponse(Page<?> page) {
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();

        // Determine the type of content
        if (!page.getContent().isEmpty() && page.getContent().get(0) instanceof Menu) {
            this.content = (List<Menu>) page.getContent();
        } else if (!page.getContent().isEmpty() && page.getContent().get(0) instanceof MenuDTO) {
            this.contentDto = (List<MenuDTO>) page.getContent();
        }
    }



}
