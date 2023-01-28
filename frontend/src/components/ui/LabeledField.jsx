import {Stack, TextField, Typography} from '@mui/material';
import React from 'react';
import {Controller} from 'react-hook-form';

const LabeledField = props => {
  const {
    control,
    id,
    label,
    rules,
    children,
    fieldStyles,
    getRootProps,
    getInputProps,
    ...otherProps
  } = props;

  return (
    <Stack sx={fieldStyles}>
      {label && (
        <Typography
          component="label"
          htmlFor={id}
          fontWeight="600"
          color="var(--text-color)"
          mb="5px">
          {label}
        </Typography>
      )}
      <Controller
        control={control}
        name={id}
        rules={rules}
        render={({field: {onChange, onBlur, value, ref}}) => (
          <TextField
            size="small"
            id={id}
            inputRef={ref}
            value={value}
            onChange={onChange}
            onBlur={onBlur}
            {...otherProps}>
            {children}
          </TextField>
        )}
      />
    </Stack>
  );
};

export default LabeledField;
