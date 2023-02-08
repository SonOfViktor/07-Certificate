import axios from 'axios';
import * as api from '../api/api';
import {configureStore} from '@reduxjs/toolkit';
import {certificatesReducer} from './certificates/certificateSlice';
import {filterReducer} from './filter/filterSlice';
import {certificateDetailsReducer} from './certificate-details/certificateDetailsSlice';
import {userReducer} from './user/userSlice';
import {categoriesReducer} from './categories/categorySlice';
import {favoriteReducer} from './favorite/favoriteSlice';
import {bucketReducer} from './bucket/bucketSlice';
import {paymentReducer} from './payment/paymentSlice';

const client = axios.create({
  timeout: 5000,
});

export const store = configureStore({
  reducer: {
    certificates: certificatesReducer,
    filter: filterReducer,
    certificateDetails: certificateDetailsReducer,
    user: userReducer,
    categories: categoriesReducer,
    bucket: bucketReducer,
    favorite: favoriteReducer,
    payments: paymentReducer,
  },
  devTools: true,
  middleware: getDefaultMiddleware =>
    getDefaultMiddleware({
      thunk: {
        extraArgument: {
          client: client,
          api,
        },
      },
      serializableCheck: false,
    }),
});
