import java.util.List;

public interface PaginationDataProvider<T> {
    int getRowCount();

    List<T> getRows(int start, int end);
}