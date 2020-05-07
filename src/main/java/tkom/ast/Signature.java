package tkom.ast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Signature {
    protected String type;
    protected String identifier;

    public void setSignature(Signature signature) {
        this.type = signature.getType();
        this.identifier = signature.getIdentifier();
    }
}
