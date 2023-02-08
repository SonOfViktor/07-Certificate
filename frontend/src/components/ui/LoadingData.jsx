import {CircularProgress, Stack} from '@mui/material';
import React from 'react';

const LoadingData = ({status = 'loading', ...props}) => {
  return (
    status === 'loading' && (
      <Stack alignItems="center" {...props}>
        <CircularProgress />
      </Stack>
    )
  );
};

export default LoadingData;
