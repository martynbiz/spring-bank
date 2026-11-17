package biz.rtyn.springbank.web;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String resource, long id) {
        super(resource + " " + id + " was not found");
    }
}
