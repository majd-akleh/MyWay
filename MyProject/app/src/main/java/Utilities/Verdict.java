package Utilities;
/*
class description:
    Verdict class is basically used to handle any type of validation
    ok: a boolean flag set to true if there is no problem otherwise it is set to false.
    status: an integer represent the problem as number or bitmask.
    description: a string  describes  the problem equals to ok if there is no problem else it may holds exception message or a description why verdict is not valid.
 */
 public class Verdict {
    private  boolean ok;
    private int status;
    private String description;

    public Verdict() {
        ok = false;
        status = -1;
        description = null;
    }

    public Verdict(boolean ok, String description) {
        this.ok = ok;
        this.status = -1;
        this.description = description;
    }

    public Verdict(boolean ok,int status ,String description) {
        this.ok = ok;
        this.status = status;
        this.description = description;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    /*this method is used to merge to verdicts where valid verdict with other valid verdict produce valid result (like bitwise AND operation)  */
    public void append(Verdict v){
        this.ok &= v.isOk();
        if(this.status == -1&&v.getStatus() == -1)this.status = -1;
        else{
            if(this.status == -1)this.status = 0;
            if(v.getStatus() == -1)v.setStatus(0);
            this.status += v.getStatus()*(1<<3);
        }
        if(this.description == null)this.description = v.getDescription();
        else if(v.getDescription() != null){
            this.description = this.description+"\n"+v.getDescription();
        }
    }
    @Override
    public String toString() {
        return "Verdict{" +
                "ok=" + ok +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
