import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';

let controller;

export const loadCertificates = createAsyncThunk(
  '@@certificates/load-certificates',
  async (
    {number, size, filter, infinite = true},
    {extra: {client, api}, rejectWithValue}
  ) => {
    controller?.abort();
    controller = new AbortController();

    try {
      const response = await client.post(
        api.certificatesUrl(number, size, filter.sort),
        {
          certificateName: filter.search,
          certificateDescription: filter.description,
          tagNames: filter.tags,
          category: filter.category,
        },
        {
          signal: controller.signal,
        }
      );

      return {response, infinite};
    } catch (e) {
      return rejectWithValue(e?.response?.data?.errorMessage || e.message);
    }
  }
);

export const createCertificate = createAsyncThunk(
  '@@certificates/create-certificate',
  async (data, {extra: {client, api}, rejectWithValue}) => {
    try {
      return await client.post(api.CREATE_CERTIFICATE, data, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: 'Bearer ' + localStorage.getItem('token'),
        },
      });
    } catch (e) {
      return rejectWithValue(e?.response?.data?.errorMessage || e.message);
    }
  }
);

export const updateCertificate = createAsyncThunk(
  '@@certificates/update-certificate',
  async (data, {extra: {client, api}, rejectWithValue}) => {
    try {
      return await client.patch(api.certificateByIdUrl(data.get('id')), data, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: 'Bearer ' + localStorage.getItem('token'),
        },
      });
    } catch (e) {
      return rejectWithValue(e?.response?.data?.errorMessage || e.message);
    }
  }
);

export const deleteCertificate = createAsyncThunk(
  '@@certificates/delete-certificate',
  async (data, {extra: {client, api}, rejectWithValue}) => {
    try {
      return await client.delete(api.certificateByIdUrl(data), {
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('token'),
        },
      });
    } catch (e) {
      return rejectWithValue(e?.response?.data?.errorMessage || e.message);
    }
  }
);

const initialState = {
  status: 'idle',
  error: '',
  certificateList: [],
  page: {number: 0, size: 20, totalElements: 0, totalPages: 0},
};

const certificateSlice = createSlice({
  name: '@@certificates',
  initialState,
  reducers: {
    setPage: (state, action) => {
      state.page.number = action.payload;
      state.status = 'idle';
    },
    changePageSize: (state, action) => {
      state.page.size = action.payload;
      state.page.number = 0;
      state.status = 'idle';
    },
    changeStatus: (state, action) => {
      state.status = action.payload;
    },
    resetCertificates: () => initialState,
    setPageByUrl: (state, action) => {
      state.page.number = action.payload?.number - 1 || 0;
      state.page.size = action.payload?.size || 20;
      state.status = 'idle';
      state.certificateList = [];
    },
  },
  extraReducers: builder => {
    builder
      .addCase(loadCertificates.pending, state => {
        state.status = 'loading';
        state.error = '';
      })
      .addCase(loadCertificates.rejected, (state, action) => {
        state.status = 'rejected';
        state.certificateList = [];
        if (action.payload !== 'canceled') {
          state.error = action.payload;
        }
      })
      .addCase(loadCertificates.fulfilled, (state, action) => {
        state.status = 'received';

        if (action.payload.infinite) {
          state.certificateList.push(
            ...action.payload.response.data._embedded.certificateTagsDtoes
          );
        } else {
          state.certificateList =
            action.payload.response.data._embedded.certificateTagsDtoes;
        }

        state.page = action.payload.response.data.page;
      })
      .addCase(createCertificate.fulfilled, state => {
        state.status = 'created';
      })
      .addCase(updateCertificate.fulfilled, state => {
        state.status = 'updated';
      })
      .addCase(deleteCertificate.fulfilled, state => {
        state.status = 'deleted';
      })
      .addMatcher(
        action => action.type.match(/^@@certificates\/.*certificate\/pending$/),
        state => {
          state.status = 'loading';
          state.error = '';
        }
      )
      .addMatcher(
        action =>
          action.type.match(/^@@certificates\/.*certificate\/rejected$/),
        (state, action) => {
          state.status = 'rejected';
          state.error = action.payload;
        }
      );
  },
});

export const {
  nextPage,
  resetCertificates,
  setPage,
  changePageSize,
  changeStatus,
  setPageByUrl,
} = certificateSlice.actions;

export const certificatesReducer = certificateSlice.reducer;
