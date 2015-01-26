package com.meitu.data.result;

import com.meitu.data.enums.RetError;
import com.meitu.data.enums.RetStatus;

public class Result<T> {
	protected T data;
	protected RetStatus status = RetStatus.SUCC;

	protected RetError err = RetError.NONE;

	public void setData(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public Result() {
	}

	public Result(RetStatus status, RetError err) {
		this.status = status;
		this.err = err;
	}

	public RetStatus getStatus() {
		return status;
	}

	public void setStatus(RetStatus status) {
		this.status = status;
	}

	public RetError getErr() {
		return err;
	}

	public void setErr(RetError err) {
		this.err = err;
	}

	public void setErr(String err) {
		this.err = RetError.convert(err);
	}

	@Override
	public String toString() {
		return "Result [ status=" + status + ", err=" + err + "]";
	}

	public static Result<?> defContentErrorResult() {
		return new Result(RetStatus.FAIL, RetError.NONE);
	}

}
