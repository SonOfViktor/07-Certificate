import {Chip, InputAdornment, Stack, Typography} from '@mui/material';
import ConfirmResetButton from '../../ui/ConfirmResetButton';
import LabeledField from '../../ui/LabeledField';
import {useTagField} from './hooks/useTagField';
import {useTags} from './hooks/useTags';

const TagsCell = ({control, isSubmitting, ...props}) => {
  const {tag, tagControl, tagTrigger, resetTag} = useTagField(isSubmitting);

  const {field, tagsErrorMessage, handleTags, handleDelete} = useTags(
    control,
    tagTrigger,
    tag,
    resetTag
  );

  return (
    <Stack>
      <LabeledField
        id="tagField"
        label="Tags"
        control={tagControl}
        {...props}
        InputProps={{
          endAdornment: (
            <InputAdornment position="end">
              <ConfirmResetButton
                sx={{marginRight: '-14px', boxShadow: 'none'}}
                size="small"
                onClick={handleTags}>
                Add
              </ConfirmResetButton>
            </InputAdornment>
          ),
        }}
      />

      <Stack direction="row" mt={1} flexWrap="wrap" gap={1} ref={field.ref}>
        {field.value.map(tagElement => (
          <Chip
            key={tagElement.id}
            label={tagElement.name}
            size="small"
            variant="outlined"
            onDelete={() => handleDelete(tagElement.id)}
          />
        ))}
      </Stack>
      {!!tagsErrorMessage && (
        <Typography color="error" fontSize="0.75rem" ml="14px" mt="4px">
          {tagsErrorMessage}
        </Typography>
      )}
    </Stack>
  );
};

export default TagsCell;
