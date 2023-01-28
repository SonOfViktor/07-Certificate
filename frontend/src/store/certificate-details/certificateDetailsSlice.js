import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';

export const loadCertificateById = createAsyncThunk(
  '@certificate-details/load-certificate-by-id',
  async (id, {extra: {client, api}}) => {
    return client.get(api.certificateByIdUrl(id));
  }
);

const initialState = {
  currentCertificate: null,
  status: 'idle',
  error: '',
};

const certificateDetailsSlice = createSlice({
  name: '@@certificate-details',
  initialState,
  reducers: {
    clearDetails: () => initialState,
  },
  extraReducers: builder => {
    builder
      .addCase(loadCertificateById.pending, state => {
        state.status = 'loading';
        state.error = '';
      })
      .addCase(loadCertificateById.rejected, (state, action) => {
        state.status = 'rejected';
        state.error = action.error.message;
      })
      .addCase(loadCertificateById.fulfilled, (state, action) => {
        state.status = 'received';
        state.currentCertificate = action.payload.data;
      });
  },
});

export const {clearDetails} = certificateDetailsSlice.actions;
export const certificateDetailsReducer = certificateDetailsSlice.reducer;
