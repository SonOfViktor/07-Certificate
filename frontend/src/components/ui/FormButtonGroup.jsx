import {Stack} from '@mui/material';
import React from 'react';
import ConfirmResetButton from './ConfirmResetButton';

const FormButtonGroup = ({
  button1,
  button2,
  onClick1,
  disabled,
  form,
  ...props
}) => {
  return (
    <Stack spacing={2} direction="row" {...props}>
      <ConfirmResetButton cancel="true" onClick={onClick1}>
        {button1}
      </ConfirmResetButton>
      <ConfirmResetButton type="submit" form={form} disabled={disabled}>
        {button2}
      </ConfirmResetButton>
    </Stack>
  );
};

export default FormButtonGroup;
