import {Stack} from '@mui/material';
import React from 'react';
import ConfirmResetButton from './ConfirmResetButton';

const TwoButtonGroup = ({
  button1,
  button2,
  onClick1,
  onClick2,
  disabled,
  ...props
}) => {
  return (
    <Stack spacing={2} direction="row" {...props}>
      <ConfirmResetButton cancel="true" onClick={onClick1}>
        {button1}
      </ConfirmResetButton>
      <ConfirmResetButton disabled={disabled} onClick={onClick2}>
        {button2}
      </ConfirmResetButton>
    </Stack>
  );
};

export default TwoButtonGroup;
