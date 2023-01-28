import {Avatar} from '@mui/material';
import React from 'react';

function stringAvatar(firstName, lastName) {
  return {
    sx: {
      backgroundColor: 'var(--text-color)',
      cursor: 'pointer',
      margin: '0 8px',
      width: 28,
      height: 28,
      fontSize: 12,
      fontWeight: 500,
      '&:hover': {
        backgroundColor: 'primary.main',
      },
      '@media screen and (max-width: 600px)': {
        width: 25,
        height: 25,
        fontSize: 10,
      },
    },
    children: `${firstName[0]}${lastName[0]}`,
  };
}

const UserAvatar = ({firstName, lastName, ...props}) => {
  return <Avatar {...stringAvatar(firstName, lastName)} {...props} />;
};

export default UserAvatar;
