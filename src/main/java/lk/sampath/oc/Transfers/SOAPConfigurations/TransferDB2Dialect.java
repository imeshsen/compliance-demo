//package lk.sampath.oc.Transfers.SOAPConfigurations;
//
//import org.hibernate.dialect.DB2Dialect;
//import org.hibernate.dialect.pagination.AbstractLimitHandler;
//import org.hibernate.dialect.pagination.LimitHandler;
//import org.hibernate.dialect.pagination.LimitHelper;
//import org.hibernate.engine.spi.RowSelection;
//
//public class TransferDB2Dialect extends DB2Dialect {
//
//    private static final AbstractLimitHandler MY_LIMIT_HANDLER = new AbstractLimitHandler() {
//        @Override
//        public String processSql(String sql, RowSelection selection) {
//            if (LimitHelper.hasFirstRow( selection )) {
//                //nest the main query in an outer select
//                return "select * from ( select inner2_.*, rownumber() over() as rownumber_ from ( "
//                        + sql + " fetch first " + getMaxOrLimit( selection ) + " rows only ) as inner2_ ) as inner1_ where rownumber_ > "
//                        + selection.getFirstRow() + " order by rownumber_";
//            }
//            return sql + " fetch first " + getMaxOrLimit( selection ) +  " rows only";
//        }
//
//        @Override
//        public boolean supportsLimit() {
//            return true;
//        }
//
//        @Override
//        public boolean useMaxForLimit() {
//            return true;
//        }
//
//        @Override
//        public boolean supportsVariableLimit() {
//            return false;
//        }
//    };
//
//    @Override
//    public boolean supportsSequences() {
//        return false;
//    }
//
//    @Override
//    public LimitHandler getLimitHandler() {
//        return MY_LIMIT_HANDLER;
//    }
//}