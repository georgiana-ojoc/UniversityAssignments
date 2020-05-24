import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PaginatedTableDecorator<T> {
    private PaginationDataProvider<T> paginationDataProvider;
    private JPanel contentPanel;
    private JPanel pageLinkPanel;
    private JTable table;
    private int[] pageSizes;
    private int currentPage = 1;
    private int currentPageSize;
    @SuppressWarnings("rawtypes")
    private CourseTableModel courseTableModel;
    private static final int pageLimit = 9;
    private static final String ellipses = "...";

    private PaginatedTableDecorator(JTable table, PaginationDataProvider<T> paginationDataProvider,
                                    int[] pageSizes, int defaultPageSize) {
        this.table = table;
        this.paginationDataProvider = paginationDataProvider;
        this.pageSizes = pageSizes;
        this.currentPageSize = defaultPageSize;
    }

    public static <T> PaginatedTableDecorator<T> decorate(JTable table, PaginationDataProvider<T> paginationDataProvider,
                                                          int[] pageSizes, int defaultPageSize) {
        PaginatedTableDecorator<T> decorator = new PaginatedTableDecorator<>(table, paginationDataProvider,
                pageSizes, defaultPageSize);
        decorator.initialize();
        return decorator;
    }

    private void initialize() {
        initializeCourseDataModel();
        initializePaginationComponents();
        initializeListeners();
        paginate();
    }

    @SuppressWarnings("rawtypes")
    private void initializeCourseDataModel() {
        TableModel tableModel = table.getModel();
        if (!(tableModel instanceof CourseTableModel)) {
            throw new IllegalArgumentException("TableModel must be a subclass of CourseTableModel.");
        }
        courseTableModel = (CourseTableModel) tableModel;
    }

    private void initializePaginationComponents() {
        contentPanel = new JPanel(new BorderLayout());
        JPanel paginationPanel = createPaginationPanel();
        contentPanel.add(paginationPanel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(table));
    }

    private void initializeListeners() {
        courseTableModel.addTableModelListener(this::refreshPageButtonPanel);
    }

    private void refreshPageButtonPanel(TableModelEvent tableModelEvent) {
        pageLinkPanel.removeAll();
        int rows = paginationDataProvider.getRowCount();
        int pages = (int) Math.ceil((double) rows / currentPageSize);
        ButtonGroup buttonGroup = new ButtonGroup();
        if (pages > pageLimit) {
            addPageButton(pageLinkPanel, buttonGroup, 1);
            if (currentPage > (pages - ((pageLimit + 1) / 2))) {
                pageLinkPanel.add(createEllipsesComponent());
                addPageButtonRange(pageLinkPanel, buttonGroup, pages - pageLimit + 3, pages);
            } else if (currentPage <= (pageLimit + 1) / 2) {
                addPageButtonRange(pageLinkPanel, buttonGroup, 2, pageLimit - 2);
                pageLinkPanel.add(createEllipsesComponent());
                addPageButton(pageLinkPanel, buttonGroup, pages);
            } else {
                pageLinkPanel.add(createEllipsesComponent());
                int start = currentPage - (pageLimit - 4) / 2;
                int end = start + pageLimit - 5;
                addPageButtonRange(pageLinkPanel, buttonGroup, start, end);
                pageLinkPanel.add(createEllipsesComponent());
                addPageButton(pageLinkPanel, buttonGroup, pages);
            }
        } else {
            addPageButtonRange(pageLinkPanel, buttonGroup, 1, pages);
        }
        pageLinkPanel.getParent().validate();
        pageLinkPanel.getParent().repaint();
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    private JPanel createPaginationPanel() {
        JPanel paginationPanel = new JPanel();
        pageLinkPanel = new JPanel(new GridLayout(1, pageLimit, 3, 3));
        paginationPanel.add(pageLinkPanel);

        if (pageSizes != null) {
            JComboBox<Integer> pageComboBox = new JComboBox<>(Arrays.stream(pageSizes).boxed().toArray(Integer[]::new));
            pageComboBox.addActionListener((ActionEvent actionEvent) -> {
                int currentPageStartRow = ((currentPage - 1) * currentPageSize) + 1;
                currentPageSize = (Integer) Objects.requireNonNull(pageComboBox.getSelectedItem());
                currentPage = ((currentPageStartRow - 1) / currentPageSize) + 1;
                paginate();
            });
            paginationPanel.add(Box.createHorizontalStrut(15));
            paginationPanel.add(new JLabel("Rows: "));
            paginationPanel.add(pageComboBox);
            pageComboBox.setSelectedItem(currentPageSize);
        }
        return paginationPanel;
    }

    private Component createEllipsesComponent() {
        return new JLabel(ellipses, SwingConstants.CENTER);
    }

    private void addPageButtonRange(JPanel parentPanel, ButtonGroup buttonGroup, int start, int end) {
        for (; start <= end; start++) {
            addPageButton(parentPanel, buttonGroup, start);
        }
    }

    private void addPageButton(JPanel parentPanel, ButtonGroup buttonGroup, int pageNumber) {
        JToggleButton toggleButton = new JToggleButton(Integer.toString(pageNumber));
        toggleButton.setMargin(new Insets(1, 3, 1, 3));
        buttonGroup.add(toggleButton);
        parentPanel.add(toggleButton);
        if (pageNumber == currentPage) {
            toggleButton.setSelected(true);
        }
        toggleButton.addActionListener(actionEvent -> {
            currentPage = Integer.parseInt(actionEvent.getActionCommand());
            paginate();
        });
    }

    @SuppressWarnings("unchecked")
    private void paginate() {
        int startIndex = (currentPage - 1) * currentPageSize;
        int endIndex = startIndex + currentPageSize;
        if (endIndex > paginationDataProvider.getRowCount()) {
            endIndex = paginationDataProvider.getRowCount();
        }
        List<T> rows = paginationDataProvider.getRows(startIndex, endIndex);
        courseTableModel.setRows(rows);
        courseTableModel.fireTableDataChanged();
    }
}
