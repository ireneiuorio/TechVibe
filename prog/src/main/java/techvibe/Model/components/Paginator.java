package techvibe.Model.components;



//Divide in pagine i record del database
public class Paginator {

    private final int limit;//quanti elementi mostrare per pagina
    private final int offset;//da che riga iniziare a leggere dal database


    public Paginator(int page,int ItemsPerPage){
        this.limit=ItemsPerPage;
        this.offset = (page - 1) * ItemsPerPage;

    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getPages(int size)
    {
        int additionalPage=(size%limit==0)?0:1;
        return (size/limit)+additionalPage;
    }


}
