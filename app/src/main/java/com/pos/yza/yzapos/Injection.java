package com.pos.yza.yzapos;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pos.yza.yzapos.data.source.CategoriesRepository;
import com.pos.yza.yzapos.data.source.PaymentsRepository;
import com.pos.yza.yzapos.data.source.ProductsRepository;
import com.pos.yza.yzapos.data.source.StaffRepository;
import com.pos.yza.yzapos.data.source.TransactionsRepository;
import com.pos.yza.yzapos.data.source.local.ProductsLocalDataSource;
import com.pos.yza.yzapos.data.source.remote.CategoriesRemoteDataSource;
import com.pos.yza.yzapos.data.source.local.CategoriesLocalDataSource;
import com.pos.yza.yzapos.data.source.remote.PaymentsRemoteDataSource;
import com.pos.yza.yzapos.data.source.remote.ProductsRemoteDataSource;
import com.pos.yza.yzapos.data.source.remote.StaffRemoteDataSource;
import com.pos.yza.yzapos.data.source.remote.TransactionsRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by David Lopez on 6/1/18.
 */

public class Injection {

    public static ProductsRepository provideProductsRepository(@NonNull Context context) {
        checkNotNull(context);
        return ProductsRepository.getInstance(ProductsLocalDataSource.getInstance(context));
        //return ProductsRepository.getInstance(ProductsRemoteDataSource.getInstance(context));
    }

    public static CategoriesRepository provideCategoriesRepository(@NonNull Context context) {
        checkNotNull(context);
        return CategoriesRepository.getInstance(CategoriesLocalDataSource.getInstance(context));
        //return CategoriesRepository.getInstance(CategoriesRemoteDataSource.getInstance(context));
    }

    public static StaffRepository provideStaffRepository(@NonNull Context context) {
        checkNotNull(context);
        return StaffRepository.getInstance(StaffRemoteDataSource.getInstance(context));
    }

    public static TransactionsRepository provideTransactionsRepository(@NonNull Context context) {
        checkNotNull(context);
        return TransactionsRepository.getInstance(TransactionsRemoteDataSource.getInstance(context));
    }

    public static PaymentsRepository providePaymentsRepository(@NonNull Context context) {
        checkNotNull(context);
        return PaymentsRepository.getInstance(PaymentsRemoteDataSource.getInstance(context));
    }

}
