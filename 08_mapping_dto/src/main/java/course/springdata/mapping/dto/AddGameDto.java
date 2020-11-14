package course.springdata.mapping.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AddGameDto {
    @Pattern(regexp = "[A-Z]+[a-z]+",
            message = "Title should start with upper letter")
    private String title;

    @DecimalMin(value = "0",message = "Price should be positive number!")
    private BigDecimal price;

    @Min(value = 0,message = "Size should be positive number!")
    private  double size;

    @Length(min = 11, max = 11, message = "Urls is not valid youtube Url! ")
    private String trailer;

    @Pattern(regexp = "http*s*:\\/\\/+\\w+.+",message = "Invalid thumbnail! https:")
    private String imageThumbnail;

    @Length(min = 20,message = "The description length must be at least 20 !")
    private String description;

    private LocalDate realiseDate;


    public AddGameDto(String title, BigDecimal price, double size, String trailer,
                      String imageThumbnail, String description, LocalDate realiseDate) {
        this.title = title;
        this.price = price;
        this.size = size;
        this.trailer = trailer;
        this.imageThumbnail = imageThumbnail;
        this.description = description;
        this.realiseDate = realiseDate;
    }

    public AddGameDto() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getRealiseDate() {
        return realiseDate;
    }

    public void setRealiseDate(LocalDate realiseDate) {
        this.realiseDate = realiseDate;
    }
}
