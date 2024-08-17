package portfolio.ecommencesite.exception;


public class ResourceNotFoundException extends RuntimeException {
    @SuppressWarnings("unused")
    private String resourceName;
    @SuppressWarnings("unused")
    private String field;
    @SuppressWarnings("unused")
    private Long fieldId;

    private String fieldEmail;

    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
        super(String.format("%s not found with %s, %s",resourceName,field,fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }
    
    public ResourceNotFoundException(String resourceName, String field, String fieldEmail) {
        super(String.format("%s not found with %s, %s",resourceName,field,fieldEmail));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldEmail = fieldEmail;
    }
}



