package example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import shim.ISmartContractStub;
import shim.SmartContractBase;

import javax.json.Json;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * fabric的转账例子
 *
 * @author lishaojie
 * @date 2018/05/23
 * @company Dingxuan
 */
public class Example02 extends SmartContractBase {

    private static Log log = LogFactory.getLog(Example02.class);
    @Override
    public SmartContractResponse init(ISmartContractStub stub) {
        return null;
    }

    @Override
    public SmartContractResponse invoke(ISmartContractStub stub) {
        try {
            final String function = stub.getFunction();
            final String[] args = stub.getParameters().toArray(new String[0]);

            switch (function) {
                case "invoke"://转账功能
                    return invoke(stub, args);
                case "query"://查询功能
                    return query(stub,args);
                case "delete":
                    return delete(stub,args);
                default:
                    return newErrorResponse(format("未知方法: %s", function));
            }
        } catch (Throwable e) {
            return newErrorResponse(e);
        }
    }

    private SmartContractResponse invoke(ISmartContractStub stub, String[] args) {
        if (args.length != 3) throw new IllegalArgumentException("参数错误");
        //转账，Ａ转账给Ｂ，金额Ｃ
        final String fromKey = args[0];//A
        final String toKey = args[1];//B
        final String amount = args[2];//C

        // 获取身份信息
        final String fromKeyState = stub.getStringState(fromKey);
        final String toKeyState = stub.getStringState(toKey);

        // 转账人余额获取类型转换
        Double fromAccountBalance = Double.parseDouble(fromKeyState);
        Double toAccountBalance = Double.parseDouble(toKeyState);

        // 转账金额类型转换
        Double transferAmount = Double.parseDouble(amount);

        // 确保金额足够
        if (transferAmount > fromAccountBalance) {
            throw new IllegalArgumentException("资金不足");
        }

        // 转账操作
        log.info(format("转账人：%s 转 %f 元给转账人：%s", fromKey,transferAmount, toKey ));
        Double newFromAccountBalance = fromAccountBalance - transferAmount;
        Double newToAccountBalance = toAccountBalance + transferAmount;
        log.info(format("各自余额为: %s = %f, %s = %f", fromKey, newFromAccountBalance, toKey, newToAccountBalance));
        stub.putStringState(fromKey, Double.toString(newFromAccountBalance));
        stub.putStringState(toKey, Double.toString(newToAccountBalance));
        log.info("转账结束.");

        return newSuccessResponse(format("成功转账 %f ,",transferAmount));
    }

    //查询
    private SmartContractResponse query(ISmartContractStub stub, String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("参数错误");

        final String accountKey = args[0];
        //账户信息查询：用户和余额
        return newSuccessResponse(Json.createObjectBuilder()
                .add("Name", accountKey)
                .add("Amount", Double.parseDouble(stub.getStringState(accountKey)))
                .build().toString().getBytes(UTF_8));

    }

    //删除用户
    private SmartContractResponse delete(ISmartContractStub stub, String[] args){
        if (args.length != 1) throw new IllegalArgumentException("参数错误");

        final String accountKey = args[0];

        stub.delState(accountKey);
        if(stub.getStringState(accountKey)!=null){
            return newErrorResponse("删除失败");
        }else{
            return newSuccessResponse(format("用户：%s,已经被删除",accountKey));
        }

    }

    @Override
    public String getSmartContractStrDescription() {
        return null;
    }
}
