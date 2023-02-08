import {createSlice} from '@reduxjs/toolkit';

const favoriteSlice = createSlice({
  name: '@@favorite',
  initialState: [],
  reducers: {
    addFavoriteCertificate: (state, action) => {
      state.push(action.payload);
    },
    deleteFavoriteCertificate: (state, action) => {
      return state.filter(certificate => {
        return certificate.giftCertificateId !== action.payload;
      });
    },
  },
});

export const {addFavoriteCertificate, deleteFavoriteCertificate} =
  favoriteSlice.actions;

export const favoriteReducer = favoriteSlice.reducer;
