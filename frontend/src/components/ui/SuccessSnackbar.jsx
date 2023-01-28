import {Alert, Snackbar} from '@mui/material';
import React, {useEffect, useState} from 'react';

const SuccessSnackbar = ({message, isShow}) => {
  const [open, setOpen] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    isShow && setOpen(isShow);
    isShow && setSuccessMessage(message);
  }, [isShow, message]);

  const handleClose = () => {
    setOpen(false);
    setSuccessMessage('');
  };

  return (
    <Snackbar open={open} autoHideDuration={5000} onClose={handleClose}>
      <Alert
        elevation={6}
        variant="filled"
        severity="success"
        color="secondary"
        sx={{width: '100%', color: 'white', fontSize: 15}}>
        {successMessage}
      </Alert>
    </Snackbar>
  );
};

export default SuccessSnackbar;
