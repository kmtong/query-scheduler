package services.bean;

import data.OutputContext;
import data.OutputResult;
import data.QueryResult;

public interface IConverter {

	OutputResult extractResult(OutputContext context, QueryResult result);

}