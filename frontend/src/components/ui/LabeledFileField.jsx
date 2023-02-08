import {
  FormControl,
  FormHelperText,
  InputAdornment,
  OutlinedInput,
  Stack,
  Typography,
} from '@mui/material';
import React from 'react';
import HoverIconButton from './HoverIconButton';
import CollectionsIcon from '@mui/icons-material/Collections';
import {Controller} from 'react-hook-form';
import Dropzone from 'react-dropzone';

const LabeledFileField = ({control, rules, errors}) => {
  return (
    <Controller
      control={control}
      name="image"
      rules={rules}
      render={({field: {onChange, onBlur, value}}) => (
        <Stack>
          <Typography
            component="label"
            htmlFor="image"
            fontWeight="600"
            color="var(--text-color)"
            mb="5px">
            Image
          </Typography>
          <Dropzone
            multiple={false}
            onDropRejected={onChange}
            onDropAccepted={onChange}
            accept={{
              'image/*': ['.jpeg', '.jpg', '.png'],
            }}>
            {({getRootProps}) => (
              <div htmlFor="image" {...getRootProps()}>
                <FormControl variant="outlined" sx={{width: '100%'}}>
                  <OutlinedInput
                    id="image"
                    size="small"
                    onBlur={onBlur}
                    value={value?.[0]?.path || value?.[0]?.file?.path || ''}
                    error={!!errors.image?.message}
                    endAdornment={
                      <InputAdornment position="end">
                        <HoverIconButton edge="end">
                          <CollectionsIcon sx={{fontSize: '1.5rem'}} />
                        </HoverIconButton>
                      </InputAdornment>
                    }
                  />
                  <FormHelperText error>{errors.image?.message}</FormHelperText>
                </FormControl>
              </div>
            )}
          </Dropzone>
        </Stack>
      )}
    />
  );
};

export default LabeledFileField;
