package catalog.report;

public class ReportParams {
  public Integer offset;
  public Integer limit;

  public ReportParams offset(Integer offset) {
    this.offset = offset;
    return this;
  }

  public ReportParams limit(Integer limit) {
    this.limit = limit;
    return this;
  }
}
