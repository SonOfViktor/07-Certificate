import {Typography} from '@mui/material';
import React from 'react';
import {Link} from 'react-router-dom';

const linkProperty = {
  '& a': {
    color: 'var(--secondary)',
  },
  '& a:hover': {
    color: 'var(--primary)',
  },
};

const SignUpLink = () => {
  return (
    <Typography mt={1} textAlign="center" sx={linkProperty}>
      You don't have an account? <Link to="/register">Create it.</Link>
    </Typography>
  );
};

export default SignUpLink;
