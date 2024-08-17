package portfolio.ecommencesite.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import portfolio.ecommencesite.dto.CategoryDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryResponse {
private List<CategoryDto> content;
private Integer pageNumber;
private Integer pageSize;
private Long totalElement;
private Integer totalPage;
private boolean lastPage;
}
