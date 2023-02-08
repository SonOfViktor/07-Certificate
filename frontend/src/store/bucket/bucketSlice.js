import {createSlice} from '@reduxjs/toolkit';

const initialState = [];

const bucketSlice = createSlice({
  name: '@@bucket',
  initialState,
  reducers: {
    addBucketCertificate: (state, action) => {
      const existingCertificate = state.find(
        cert => cert.giftCertificateId === action.payload.giftCertificateId
      );

      if (existingCertificate) {
        existingCertificate.amount++;
      } else {
        state.push({...action.payload, amount: 1});
      }
    },
    deleteBucketCertificate: (state, action) => {
      const existingCertificate = state.find(
        cert => cert.giftCertificateId === action.payload
      );

      if (existingCertificate.amount > 1) {
        existingCertificate.amount--;
      } else {
        return state.filter(cert => cert.giftCertificateId !== action.payload);
      }
    },
    resetBucket: () => {
      return initialState;
    },
  },
});

export const {addBucketCertificate, deleteBucketCertificate, resetBucket} =
  bucketSlice.actions;
export const bucketReducer = bucketSlice.reducer;
