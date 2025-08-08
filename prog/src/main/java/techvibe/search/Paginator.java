package techvibe.search;

public class Paginator {

    private final int limit;
    private final int offeset;


    public Paginator(int page,int ItemsPerPage){
        this.limit=ItemsPerPage;
        this.offeset=(page-1)*ItemsPerPage+1;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffeset() {
        return offeset;
    }
}
