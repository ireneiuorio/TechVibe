package techvibe.components;

public class Paginator {

    private final int limit;
    private final int offeset;


    public Paginator(int page,int ItemsPerPage){
        this.limit=ItemsPerPage;
        this.offeset=(page==1)? 0:(page-1)*ItemsPerPage+1;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offeset;
    }

    public int getPages(int size)
    {
        int additionalPage=(size%limit==0)?0:1;
        return (size/limit)+additionalPage;
    }
}
